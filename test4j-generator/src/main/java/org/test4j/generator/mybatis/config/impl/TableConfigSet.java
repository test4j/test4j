package org.test4j.generator.mybatis.config.impl;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.config.ITableConfigSet;
import org.test4j.generator.mybatis.config.ITableInfoSet;
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
public class TableConfigSet implements ITableConfigSet {
    private final GlobalConfig globalConfig;
    /**
     * 需要处理的表
     */
    @Getter
    private Map<String, TableInfoSet> tables = new HashMap<>();


    public TableConfigSet(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    @Override
    public ITableConfigSet table(String tableName) {
        return this.table(tableName, (table) -> {
        });
    }

    @Override
    public ITableConfigSet table(String tableName, Consumer<ITableInfoSet> consumer) {
        TableInfoSet table = new TableInfoSet(tableName, this.globalConfig, this);
        consumer.accept(table);
        this.tables.put(tableName, table);
        return this;
    }

    @Override
    public void foreach(Consumer<ITableInfoSet> consumer) {
        this.tables.values().stream().forEach(table -> consumer.accept(table));
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
                    TableInfoSet tableInfo = this.getTables().get(tableName);
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
            for (Map.Entry<String, TableInfoSet> entry : this.getTables().entrySet()) {
                entry.getValue().initTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String selectTableSql(TableConfigSet config) {
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