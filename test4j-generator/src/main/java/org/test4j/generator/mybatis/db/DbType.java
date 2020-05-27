package org.test4j.generator.mybatis.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.test4j.generator.mybatis.db.convert.*;
import org.test4j.generator.mybatis.db.query.*;

/**
 * 数据库类型
 *
 * @author wudarui
 */
@Getter
@AllArgsConstructor
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql", "MySql数据库") {
        @Override
        public AbstractDbQuery newQuery() {
            return new MySqlQuery();
        }

        @Override
        public ITypeConvert newConvert() {
            return new MySqlTypeConvert();
        }
    },
    /**
     * MARIADB
     */
    MARIADB("mariadb", "MariaDB数据库") {
        @Override
        public AbstractDbQuery newQuery() {
            return new MariadbQuery();
        }

        @Override
        public ITypeConvert newConvert() {
            return new MySqlTypeConvert();
        }
    },
    /**
     * ORACLE
     */
    ORACLE("oracle", "Oracle数据库") {
        @Override
        public AbstractDbQuery newQuery() {
            return new OracleQuery();
        }

        @Override
        public ITypeConvert newConvert() {
            return new OracleTypeConvert();
        }
    },
    /**
     * DB2
     */
    DB2("db2", "DB2数据库") {
        @Override
        public AbstractDbQuery newQuery() {
            return new DB2Query();
        }

        @Override
        public ITypeConvert newConvert() {
            return new DB2TypeConvert();
        }
    },
    /**
     * H2
     */
    H2("h2", "H2数据库") {
        @Override
        public AbstractDbQuery newQuery() {
            return new H2Query();
        }

        @Override
        public ITypeConvert newConvert() {
            return new MySqlTypeConvert();
        }
    },
    /**
     * SQLITE
     */
    SQLITE("sqlite", "SQLite数据库") {
        @Override
        public AbstractDbQuery newQuery() {
            return new SqliteQuery();
        }

        @Override
        public ITypeConvert newConvert() {
            return new SqliteTypeConvert();
        }

        /**
         * SQLITE 数据库不支持注释获取
         * @return
         */
        @Override
        public boolean isCommentSupported() {
            return false;
        }
    },
    /**
     * POSTGRE
     */
    POSTGRE_SQL("postgresql", "Postgre数据库") {
        @Override
        public AbstractDbQuery newQuery() {
            return new PostgreSqlQuery();
        }

        @Override
        public ITypeConvert newConvert() {
            return new PostgreSqlTypeConvert();
        }
    },
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver", "SQLServer数据库") {
        @Override
        public AbstractDbQuery newQuery() {
            return new SqlServerQuery();
        }

        @Override
        public ITypeConvert newConvert() {
            return new SqlServerTypeConvert();
        }
    };

    /**
     * 数据库名称
     */
    private final String db;
    /**
     * 描述
     */
    private final String desc;

    public abstract AbstractDbQuery newQuery();

    public abstract ITypeConvert newConvert();

    /**
     * 数据库是否支持获取注释
     *
     * @return
     */
    public boolean isCommentSupported() {
        return true;
    }

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