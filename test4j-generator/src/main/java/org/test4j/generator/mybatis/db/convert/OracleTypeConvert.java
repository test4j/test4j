package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.ITypeConvert;

/**
 * ORACLE 字段类型转换
 */
public class OracleTypeConvert implements ITypeConvert {

    @Override
    public Class processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return String.class;
        } else if (t.contains("date") || t.contains("timestamp")) {
            switch (dateType) {
                case ONLY_DATE:
                    return java.util.Date.class;
                case SQL_PACK:
                    return java.sql.Timestamp.class;
                case TIME_PACK:
                    return java.time.LocalDateTime.class;
            }
        } else if (t.contains("number")) {
            if (t.matches("number\\(+\\d\\)")) {
                return Integer.class;
            } else if (t.matches("number\\(+\\d{2}+\\)")) {
                return Long.class;
            }
            return java.math.BigDecimal.class;
        } else if (t.contains("float")) {
            return Float.class;
        } else if (t.contains("clob")) {
            return String.class;
        } else if (t.contains("blob")) {
            return java.sql.Blob.class;
        } else if (t.contains("binary")) {
            return byte[].class;
        } else if (t.contains("raw")) {
            return byte[].class;
        }
        return String.class;
    }
}