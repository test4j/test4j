package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.ColumnJavaType;
import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.ITypeConvert;

/**
 * ORACLE 字段类型转换
 *
 */
public class OracleTypeConvert implements ITypeConvert {

    @Override
    public ColumnJavaType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return ColumnJavaType.STRING;
        } else if (t.contains("date") || t.contains("timestamp")) {
            switch (dateType) {
                case ONLY_DATE:
                    return ColumnJavaType.DATE;
                case SQL_PACK:
                    return ColumnJavaType.TIMESTAMP;
                case TIME_PACK:
                    return ColumnJavaType.LOCAL_DATE_TIME;
            }
        } else if (t.contains("number")) {
            if (t.matches("number\\(+\\d\\)")) {
                return ColumnJavaType.INTEGER;
            } else if (t.matches("number\\(+\\d{2}+\\)")) {
                return ColumnJavaType.LONG;
            }
            return ColumnJavaType.BIG_DECIMAL;
        } else if (t.contains("float")) {
            return ColumnJavaType.FLOAT;
        } else if (t.contains("clob")) {
            return ColumnJavaType.STRING;
        } else if (t.contains("blob")) {
            return ColumnJavaType.BLOB;
        } else if (t.contains("binary")) {
            return ColumnJavaType.BYTE_ARRAY;
        } else if (t.contains("raw")) {
            return ColumnJavaType.BYTE_ARRAY;
        }
        return ColumnJavaType.STRING;
    }

}