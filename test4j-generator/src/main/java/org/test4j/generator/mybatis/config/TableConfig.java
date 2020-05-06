package org.test4j.generator.mybatis.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.DbType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 全局构建配置项
 *
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class TableConfig {
    private GlobalConfig globalConfig;

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;
    /**
     * 时间类型对应策略
     */
    private DateType dateType = DateType.ONLY_DATE;
    /**
     * 开启 baseColumnList
     */
    private boolean baseColumnList = false;
    /**
     * 需要去掉的表前缀
     */
    @Setter(AccessLevel.NONE)
    private String[] tablePrefix;

    /**
     * 需要处理的表
     */
    @Getter
    private Map<String, TableInfo> tables = new HashMap<>();

    public TableConfig() {
    }


    public TableInfo table(String tableName) {
        TableInfo table = new TableInfo(tableName, this);
        this.tables.put(tableName, table);
        return table;
    }

    public TableConfig addTable(TableInfo table) {
        this.tables.put(table.getTableName(), table);
        return this;
    }

    public TableConfig addTable(String tableName) {
        TableInfo table = new TableInfo(tableName, this);
        this.tables.put(tableName, table);
        return this;
    }

    public TableConfig addTable(String tableName, boolean isPartition) {
        TableInfo table = new TableInfo(tableName, this)
            .setPartition(isPartition);
        this.tables.put(tableName, table);
        return this;
    }

    /**
     * 对所有表统一处理
     *
     * @param consumer
     * @return
     */
    public TableConfig allTable(Consumer<TableInfo> consumer) {
        this.tables.values().stream().forEach(table -> consumer.accept(table));
        return this;
    }

    public TableConfig setTablePrefix(String... tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    /**
     * 是否需要做移除前缀处理
     *
     * @return
     */
    public boolean needRemovePrefix() {
        return this.tablePrefix != null && this.tablePrefix.length != 0;
    }

    /**
     * 获取所有的数据库表信息
     *
     * @return
     */
    public void initTables() {
        DbConfig dbConfig = this.globalConfig.getDbConfig();
        try {
            String tablesSql = selectTableSql(this);
            Set<String> existed = new HashSet<>();
            try (PreparedStatement preparedStatement = dbConfig.getConn().prepareStatement(tablesSql); ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    String tableName = results.getString(dbConfig.getDbQuery().tableName());
                    TableInfo tableInfo = this.getTables().get(tableName);
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

            Set<String> all = this.getTables().keySet();
            for (String table : all) {
                if (!existed.contains(table)) {
                    System.err.println("表 " + table + " 在数据库中不存在！！！");
                    this.getTables().remove(table);
                }
            }
            for (Map.Entry<String, TableInfo> entry : this.getTables().entrySet()) {
                entry.getValue().setConfig(this.globalConfig, this);
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
}
