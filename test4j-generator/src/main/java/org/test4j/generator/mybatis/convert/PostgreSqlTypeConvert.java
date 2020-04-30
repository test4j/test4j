package org.test4j.generator.mybatis.convert;

import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.test4j.generator.mybatis.rule.IColumnType;

/**
 * PostgreSQL 字段类型转换
 *
 * @author hubin
 * @since 2017-01-20
 */
public class PostgreSqlTypeConvert implements ITypeConvert {

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return ColumnType.STRING;
        } else if (t.contains("bigint")) {
            return ColumnType.LONG;
        } else if (t.contains("int")) {
            return ColumnType.INTEGER;
        } else if (t.contains("date") || t.contains("time")) {
            switch (globalConfig.getDateType()) {
                case ONLY_DATE:
                    return ColumnType.DATE;
                case SQL_PACK:
                    switch (t) {
                        case "date":
                            return ColumnType.DATE_SQL;
                        case "time":
                            return ColumnType.TIME;
                        default:
                            return ColumnType.TIMESTAMP;
                    }
                case TIME_PACK:
                    switch (t) {
                        case "date":
                            return ColumnType.LOCAL_DATE;
                        case "time":
                            return ColumnType.LOCAL_TIME;
                        default:
                            return ColumnType.LOCAL_DATE_TIME;
                    }
                default:
                    return ColumnType.DATE;
            }
        } else if (t.contains("text")) {
            return ColumnType.STRING;
        } else if (t.contains("bit")) {
            return ColumnType.BOOLEAN;
        } else if (t.contains("decimal")) {
            return ColumnType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return ColumnType.CLOB;
        } else if (t.contains("blob")) {
            return ColumnType.BYTE_ARRAY;
        } else if (t.contains("float")) {
            return ColumnType.FLOAT;
        } else if (t.contains("double")) {
            return ColumnType.DOUBLE;
        } else if (t.contains("json") || t.contains("enum")) {
            return ColumnType.STRING;
        } else if (t.contains("boolean")) {
            return ColumnType.BOOLEAN;
        } else if (t.contains("numeric")) {
            return ColumnType.BIG_DECIMAL;
        }
        return ColumnType.STRING;
    }
}
