package org.test4j.generator.mybatis;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.engine.AbstractTemplateEngine;
import org.test4j.generator.engine.VelocityTemplateEngine;
import org.test4j.generator.mybatis.config.TableConfig;
import org.test4j.generator.mybatis.config.DbConfig;
import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.db.DbType;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.TemplateList;
import org.test4j.generator.mybatis.template.summary.SummaryTemplate;
import org.test4j.hamcrest.Assert;
import org.test4j.tools.commons.StringHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.test4j.generator.mybatis.config.constant.ConfigKey.*;
import static org.test4j.module.core.utility.MessageHelper.info;

/**
 * 数据库表列表
 *
 * @author wudarui
 */
@Slf4j
@Data
@Accessors(chain = true)
public class MyBatisGenerator {
    @Getter(AccessLevel.NONE)
    private AbstractTemplateEngine templateEngine = new VelocityTemplateEngine();

    @Setter(AccessLevel.NONE)
    private List<TableConfig> tableConfigs = new ArrayList<>();
    /**
     * 全局配置
     */
    private GlobalConfig globalConfig;

    public MyBatisGenerator(TableConfig... tableConfigs) {
        for (TableConfig config : tableConfigs) {
            config.setGlobalConfig(globalConfig);
            this.tableConfigs.add(config);
        }
    }

    public void execute() {
        if (globalConfig == null) {
            throw new RuntimeException("the global config not set.");
        }
        if (tableConfigs == null || tableConfigs.isEmpty()) {
            throw new RuntimeException("the table config not set.");
        }
        List<Map<String, Object>> allContext = new ArrayList<>();
        for (TableConfig config : this.tableConfigs) {
            info("===数据库元信息初始化...");
            this.initTableInfos(config);
            info("===准备生成文件...");
            for (Map.Entry<String, TableInfo> entry : config.getTables().entrySet()) {
                info("======处理表：" + entry.getKey());
                TableInfo tableInfo = entry.getValue();
                // 初始化各个模板需要的变量
                Map<String, Object> context = tableInfo.initTemplateContext();
                for (BaseTemplate template : TemplateList.ALL_TEMPLATES) {
                    Map<String, Object> templateContext = template.initContext(tableInfo);
                    if (KEY_ENTITY.equals(template.getTemplateId())) {
                        context.put(KEY_ENTITY_NAME, templateContext.get(KEY_NAME));
                    }
                    context.put(template.getTemplateId(), templateContext);
                }
                for (BaseTemplate template : TemplateList.ALL_TEMPLATES) {
                    if (template.isPartition() && !tableInfo.isPartition()) {
                        continue;
                    }
                    String filePath = template.getFilePath();
                    info("=========生成文件: " + template.getFileNameReg());
                    Assert.notNull(filePath, "文件路径不能为空,[table=%s,template=%s]", tableInfo.getTableName(), template.getTemplate());
                    templateEngine.output(template.getTemplate(), context, filePath);
                }
                allContext.add(context);
            }
            info("===文件生成完成！！！");
            this.open();
        }
        Map<String, Object> wrapper = new HashMap<>();
        {
            wrapper.put("configs", allContext);
            wrapper.put("basePackage", this.globalConfig.getBasePackage());
        }
        for (SummaryTemplate summary : TemplateList.summaries) {
            summary.setGlobalConfig(this.globalConfig);
            templateEngine.output(summary.getTemplateId(), wrapper, summary.getFilePath());
        }
    }

    /**
     * 获取所有的数据库表信息
     *
     * @param config
     * @return
     */
    private void initTableInfos(TableConfig config) {
        DbConfig dbConfig = this.globalConfig.getDbConfig();
        try {
            String tablesSql = selectTableSql(config);
            Set<String> existed = new HashSet<>();
            try (PreparedStatement preparedStatement = dbConfig.getConn().prepareStatement(tablesSql); ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    String tableName = results.getString(dbConfig.getDbQuery().tableName());
                    TableInfo tableInfo = config.getTables().get(tableName);
                    if (tableInfo == null) {
                        continue;
                    }
                    existed.add(tableName);
                    if (dbConfig.getDbType().isCommentSupported()) {
                        String tableComment = results.getString(dbConfig.getDbQuery().tableComment());
                        tableInfo.setComment(tableComment);
                    }
                }
            }

            Set<String> all = config.getTables().keySet();
            for (String table : all) {
                if (!existed.contains(table)) {
                    System.err.println("表 " + table + " 在数据库中不存在！！！");
                    config.getTables().remove(table);
                }
            }
            for (Map.Entry<String, TableInfo> entry : config.getTables().entrySet()) {
                entry.getValue().setConfig(this.globalConfig, config);
                entry.getValue().initTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String selectTableSql(TableConfig config) {
        DbConfig dbConfig = this.globalConfig.getDbConfig();
        DbType dbType = this.globalConfig.getDbType();
        String tablesSql = dbConfig.getDbQuery().tablesSql();
        String schema = dbConfig.getSchemaName();

        switch (dbType) {
            case POSTGRE_SQL:
                if (schema == null) {
                    schema = "public";
                    dbConfig.setSchemaName(schema);
                }
                return String.format(tablesSql, schema);
            case DB2:
                if (schema == null) {
                    schema = "current schema";
                    dbConfig.setSchemaName(schema);
                }
                return String.format(tablesSql, schema);
            case ORACLE:
                //oracle 默认 schema=username
                if (schema == null) {
                    schema = dbConfig.getUsername().toUpperCase();
                    dbConfig.setSchemaName(schema);
                }
                String tables = config.getTables().keySet().stream().map(String::toUpperCase).map(table -> "'" + table + "'").collect(Collectors.joining(", "));
                return new StringBuilder(String.format(tablesSql, schema))
                    .append(" AND ").append(dbConfig.getDbQuery().tableName()).append(" IN (")
                    .append(tables)
                    .append(")")
                    .toString();
            default:
                return tablesSql;
        }
    }

    /**
     * 打开输出目录
     */
    private void open() {
        try {
            if (globalConfig.isOpen() && !StringHelper.isBlank(globalConfig.getOutputDir())) {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + globalConfig.getOutputDir());
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + globalConfig.getOutputDir());
                    } else {
                        log.debug("文件输出目录:" + globalConfig.getOutputDir());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
