package org.test4j.generator.mybatis.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.engine.AbstractTemplateEngine;
import org.test4j.generator.engine.VelocityTemplateEngine;
import org.test4j.generator.mybatis.config.BuildConfig;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.config.StrategyConfig;
import org.test4j.generator.mybatis.rule.DbType;
import org.test4j.generator.mybatis.config.DataSourceConfig;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.TemplateList;
import org.test4j.hamcrest.Assert;
import org.test4j.tools.commons.StringHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.test4j.module.core.utility.MessageHelper.info;

/**
 * 数据库表列表
 *
 * @author wudarui
 */
@Slf4j
@Setter
@Getter
public class Generator {
    private AbstractTemplateEngine templateEngine = new VelocityTemplateEngine();

    @Setter(AccessLevel.NONE)
    private List<BuildConfig> configs = new ArrayList<>();

    public Generator(BuildConfig... configs) {
        for (BuildConfig config : configs) {
            config.setGenerator(this);
            this.configs.add(config);
        }
    }

    public void execute() {
        List<GenerateObj> generateObjs = new ArrayList<>();
        for (BuildConfig config : this.configs) {
            templateEngine.init(config);
            info("==========================数据库元信息初始化...=====================");
            this.initTableInfos(config);
            info("==========================准备生成文件...==========================");
            for (Map.Entry<String, TableInfo> entry : config.getTables().entrySet()) {
                info("==========================处理表：" + entry.getKey());
                TableInfo tableInfo = entry.getValue();
                // 初始化各个模板需要的变量
                Map<String, Object> context = tableInfo.initTemplateContext();
                for (BaseTemplate template : TemplateList.ALL_TEMPLATES) {
                    template.initContext(tableInfo, context);
                }
                for (BaseTemplate template : TemplateList.ALL_TEMPLATES) {
                    if (template.isPartition() && !tableInfo.isPartition()) {
                        continue;
                    }
                    String filePath = template.getFilePath();
                    info("==========================生成文件: " + template.getFileNameReg());
                    Assert.notNull(filePath, "文件路径不能为空,[table=%s,template=%s]", tableInfo.getTableName(), template.getTemplate());
                    templateEngine.output(template.getTemplate(), context, filePath);
                }
                generateObjs.add(GenerateObj.init(tableInfo));
            }
            info("==========================文件生成完成！！！==========================");
            open(config);
        }
        GenerateObj.generate(generateObjs, outputDir, testOutputDir, basePackage);
    }

    /**
     * 获取所有的数据库表信息
     *
     * @param config
     * @return
     */
    private void initTableInfos(BuildConfig config) {
        try {
            String tablesSql = selectTableSql(config);
            Set<String> existed = new HashSet<>();
            try (PreparedStatement preparedStatement = dataSourceConfig.getConn().prepareStatement(tablesSql); ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    String tableName = results.getString(dataSourceConfig.getDbQuery().tableName());
                    TableInfo tableInfo = config.getTables().get(tableName);
                    if (tableInfo == null) {
                        continue;
                    }
                    existed.add(tableName);
                    if (this.isCommentSupported()) {
                        String tableComment = results.getString(dataSourceConfig.getDbQuery().tableComment());
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
                entry.getValue().setConfig(config, this);
                entry.getValue().initTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String selectTableSql(BuildConfig config) {
        DbType dbType = dataSourceConfig.getDbQuery().dbType();
        String tablesSql = dataSourceConfig.getDbQuery().tablesSql();
        String schema = dataSourceConfig.getSchemaName();

        switch (dbType) {
            case POSTGRE_SQL:
                if (schema == null) {
                    schema = "public";
                    dataSourceConfig.setSchemaName(schema);
                }
                return String.format(tablesSql, schema);
            case DB2:
                if (schema == null) {
                    schema = "current schema";
                    dataSourceConfig.setSchemaName(schema);
                }
                return String.format(tablesSql, schema);
            case ORACLE:
                //oracle 默认 schema=username
                if (schema == null) {
                    schema = dataSourceConfig.getUsername().toUpperCase();
                    dataSourceConfig.setSchemaName(schema);
                }
                String tables = config.getTables().keySet().stream().map(String::toUpperCase).map(table -> "'" + table + "'").collect(Collectors.joining(", "));
                return new StringBuilder(String.format(tablesSql, schema))
                    .append(" AND ").append(dataSourceConfig.getDbQuery().tableName()).append(" IN (")
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
    private void open(BuildConfig config) {
        try {
            if (config.isOpen() && !StringHelper.isBlank(this.outputDir)) {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + this.outputDir);
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + this.outputDir);
                    } else {
                        log.debug("文件输出目录:" + this.outputDir);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 代码生成路径
     */
    @Setter(AccessLevel.NONE)
    private String outputDir = System.getProperty("user.dir") + "/target/generate/base";
    /**
     * 测试代码生成路径
     */
    @Setter(AccessLevel.NONE)
    private String testOutputDir = System.getProperty("user.dir") + "/target/generate/test";
    /**
     * dao代码生成路径
     */
    @Setter(AccessLevel.NONE)
    private String daoOutputDir = System.getProperty("user.dir") + "/target/generate/dao";

    public Generator setOutputDir(String outputDir, String testOutputDir, String daoOutputDir) {
        this.outputDir = outputDir;
        this.testOutputDir = testOutputDir;
        this.daoOutputDir = daoOutputDir;
        return this;
    }

    /**
     * 数据源配置
     */
    private DataSourceConfig dataSourceConfig;

    public Generator setDataSource(String url, String username, String password) {
        return this.setDataSource(url, username, password, null);
    }

    public Generator setDataSource(String url, String username, String password, ITypeConvert typeConvert) {
        this.dataSourceConfig = new DataSourceConfig(DbType.MYSQL, "com.mysql.jdbc.Driver", url, username, password)
            .setTypeConvert(typeConvert);
        return this;
    }

    /**
     * 是否支持注释
     */
    @Getter(AccessLevel.NONE)
    private boolean commentSupported = true;

    public boolean isCommentSupported() {
        //SQLITE 数据库不支持注释获取
        return commentSupported && !dataSourceConfig.getDbType().equals(DbType.SQLITE);
    }

    /**
     * 命名策略
     */
    private StrategyConfig strategyConfig = new StrategyConfig();
    /**
     * 代码package前缀
     */
    private String basePackage;

    public Generator setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }
}
