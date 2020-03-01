package org.test4j.module.database.utility;

import org.test4j.exception.UnConfigDataBaseTypeException;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.StringHelper;

public enum DataSourceType {
    /**
     * H2Db
     */
    H2DB() {
        @Override
        public boolean isMemoryDB() {
            return true;
        }
    },
    /**
     * HsqlDb
     */
    HSQLDB() {
        @Override
        public boolean isMemoryDB() {
            return true;
        }
    },
    MySql(),
    MariaDB() {
        @Override
        public boolean isMemoryDB() {
            return true;
        }
    },

    Oracle(),

    SqlServer(),

    DerbyDB(),

    DB2(),

    UnSupport();

    public boolean isMemoryDB() {
        return false;
    }


    /**
     * 根据配置查找对应的数据库类型<br>
     * type=null || "",表示配置文件中设置的默认数据库
     *
     * @param type
     * @return
     */
    public static DataSourceType databaseType(final String type) {
        String _type = type;
        if (StringHelper.isBlankOrNull(type)) {
            _type = ConfigHelper.defaultDatabaseType();
        }
        if (StringHelper.isBlankOrNull(_type)) {
            throw new UnConfigDataBaseTypeException("please config 'db.dataSource.type' in file[test4j.properties]");
        }
        try {
            DataSourceType dbType = DataSourceType.valueOf(_type.toUpperCase());
            return dbType;
        } catch (Throwable e) {
            throw new RuntimeException("unknown database type", e);
        }
    }
}
