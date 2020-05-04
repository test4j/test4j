package org.test4j.generator.mybatis.convert;

import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.test4j.generator.mybatis.rule.DateType;
import org.test4j.generator.mybatis.model.IJavaType;

/**
 * ORACLE 字段类型转换
 *
 * @author wudarui
 */
public class OracleTypeConvert implements ITypeConvert {

    @Override
    public IJavaType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return ColumnType.STRING;
        } else if (t.contains("date") || t.contains("timestamp")) {
            switch (dateType) {
                case ONLY_DATE:
                    return ColumnType.DATE;
                case SQL_PACK:
                    return ColumnType.TIMESTAMP;
                case TIME_PACK:
                    return ColumnType.LOCAL_DATE_TIME;
            }
        } else if (t.contains("number")) {
            if (t.matches("number\\(+\\d\\)")) {
                return ColumnType.INTEGER;
            } else if (t.matches("number\\(+\\d{2}+\\)")) {
                return ColumnType.LONG;
            }
            return ColumnType.BIG_DECIMAL;
        } else if (t.contains("float")) {
            return ColumnType.FLOAT;
        } else if (t.contains("clob")) {
            return ColumnType.STRING;
        } else if (t.contains("blob")) {
            return ColumnType.BLOB;
        } else if (t.contains("binary")) {
            return ColumnType.BYTE_ARRAY;
        } else if (t.contains("raw")) {
            return ColumnType.BYTE_ARRAY;
        }
        return ColumnType.STRING;
    }

}
