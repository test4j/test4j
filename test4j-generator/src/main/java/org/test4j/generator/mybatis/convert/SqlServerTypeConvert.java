package org.test4j.generator.mybatis.convert;

import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.test4j.generator.mybatis.rule.IColumnType;

/**
 * SQLServer 字段类型转换
 *
 * @author hubin
 * @since 2017-01-20
 */
public class SqlServerTypeConvert implements ITypeConvert {

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char") || t.contains("xml")) {
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
        } else if (t.contains("decimal") || t.contains("numeric")) {
            return ColumnType.DOUBLE;
        } else if (t.contains("money")) {
            return ColumnType.BIG_DECIMAL;
        } else if (t.contains("binary") || t.contains("image")) {
            return ColumnType.BYTE_ARRAY;
        } else if (t.contains("float") || t.contains("real")) {
            return ColumnType.FLOAT;
        }
        return ColumnType.STRING;
    }

}
