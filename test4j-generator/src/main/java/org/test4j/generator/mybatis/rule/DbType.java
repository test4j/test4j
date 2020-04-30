package org.test4j.generator.mybatis.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库类型
 */
@Getter
@AllArgsConstructor
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql", "MySql数据库"),
    /**
     * MARIADB
     */
    MARIADB("mariadb", "MariaDB数据库"),
    /**
     * ORACLE
     */
    ORACLE("oracle", "Oracle数据库"),
    /**
     * DB2
     */
    DB2("db2", "DB2数据库"),
    /**
     * H2
     */
    H2("h2", "H2数据库"),
    /**
     * SQLITE
     */
    SQLITE("sqlite", "SQLite数据库"),
    /**
     * POSTGRE
     */
    POSTGRE_SQL("postgresql", "Postgre数据库"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver", "SQLServer数据库");

    /**
     * 数据库名称
     */
    private final String db;
    /**
     * 描述
     */
    private final String desc;

    /**
     * 判断数据库类型（默认 MySql）
     *
     * @param driver driverName 或小写后的 url
     */
    public static DbType getDbType(String driver) {
        if (driver == null) {
            return MYSQL;
        }
        String temp = driver.toLowerCase();
        DbType[] dbTypes = DbType.values();
        for (DbType type : dbTypes) {
            if (temp.contains(type.db)) {
                return type;
            }
        }
        return MYSQL;
    }
}
