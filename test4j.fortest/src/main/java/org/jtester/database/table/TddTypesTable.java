package org.jtester.database.table;

import org.jtester.module.ICore.DataMap;

/**
 * 数据库 tdd_types 表数据准备或验证
 * 
 * @author darui.wudr 2013-1-8 下午5:33:49
 */
@SuppressWarnings("serial")
public class TddTypesTable extends DataMap {
    public static interface IColumn {
        final String f_TINYINT          = "TINYINT";
        final String f_TINYINT_UNSIGNED = "TINYINT_UNSIGNED";
        final String f_SMALLINT         = "SMALLINT";
        final String f_MEDIUMINT        = "MEDIUMINT";
        final String f_INTEGER          = "INTEGER";
        final String f_BIGINT           = "BIGINT";
        final String f_FLOAT            = "FLOAT";
        final String f_DOUBLE           = "DOUBLE";
        final String f_DECIMAL          = "DECIMAL";
        final String f_DATE             = "DATE";
        final String f_DATETIME         = "DATETIME";
        final String f_TIME             = "TIME";
        final String f_YEAR             = "YEAR";
        final String f_CHAR             = "CHAR";
        final String f_VARCHAR          = "VARCHAR";
        final String f_TINYBLOB         = "TINYBLOB";
        final String f_BLOB             = "BLOB";
        final String f_MEDIUMBLOB       = "MEDIUMBLOB";
        final String f_LONGBLOB         = "LONGBLOB";
        final String f_TINYTEXT         = "TINYTEXT";
        final String f_TEXT             = "TEXT";
        final String f_MEDIUMTEXT       = "MEDIUMTEXT";
        final String f_LONGTEXT         = "LONGTEXT";
        final String f_SET              = "SET";
        final String f_BINARY           = "BINARY";
        final String f_VARBINARY        = "VARBINARY";
        final String f_BIT              = "BIT";
        final String f_BOOLEAN          = "BOOLEAN";
        final String f_ENUM             = "ENUM";
    }
}
