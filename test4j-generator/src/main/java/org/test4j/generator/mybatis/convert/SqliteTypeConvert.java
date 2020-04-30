package org.test4j.generator.mybatis.convert;

import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.test4j.generator.mybatis.rule.IColumnType;
/**
 * SQLite 字段类型转换
 *
 * @author chen_wj
 * @since 2019-05-08
 */
public class SqliteTypeConvert implements ITypeConvert {
    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("bigint")) {
            return ColumnType.LONG;
        } else if (t.contains("tinyint(1)") || t.contains("boolean")) {
            return ColumnType.BOOLEAN;
        } else if (t.contains("int")) {
            return ColumnType.INTEGER;
        } else if (t.contains("text") || t.contains("char") || t.contains("enum") ) {
            return ColumnType.STRING;
        } else if (t.contains("decimal") || t.contains("numeric")) {
            return ColumnType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return ColumnType.CLOB;
        } else if (t.contains("blob")) {
            return ColumnType.BLOB;
        } else if (t.contains("float")) {
            return ColumnType.FLOAT;
        } else if (t.contains("double")) {
            return ColumnType.DOUBLE;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            switch (globalConfig.getDateType()) {
                case ONLY_DATE:
                    return ColumnType.DATE;
                case SQL_PACK:
                    switch (t) {
                        case "date":
                            return ColumnType.DATE_SQL;
                        case "time":
                            return ColumnType.TIME;
                        case "year":
                            return ColumnType.DATE_SQL;
                        default:
                            return ColumnType.TIMESTAMP;
                    }
                case TIME_PACK:
                    switch (t) {
                        case "date":
                            return ColumnType.LOCAL_DATE;
                        case "time":
                            return ColumnType.LOCAL_TIME;
                        case "year":
                            return ColumnType.YEAR;
                        default:
                            return ColumnType.LOCAL_DATE_TIME;
                    }
            }
        }
        return ColumnType.STRING;
    }
}
