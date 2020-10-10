package org.test4j.generator.db.convert;

import org.test4j.generator.db.DateType;

/**
 * PostgreSQL 字段类型转换
 */
public class PostgreSqlTypeConvert extends BaseTypeConvert {

    @Override
    public Class processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return String.class;
        } else if (t.contains("bigint")) {
            return Long.class;
        } else if (t.contains("int")) {
            return Integer.class;
        } else if (t.contains("date") || t.contains("time")) {
            return this.parseDateType(dateType, t);
        } else if (t.contains("text")) {
            return String.class;
        } else if (t.contains("bit")) {
            return Boolean.class;
        } else if (t.contains("decimal")) {
            return java.math.BigDecimal.class;
        } else if (t.contains("clob")) {
            return java.sql.Clob.class;
        } else if (t.contains("blob")) {
            return byte[].class;
        } else if (t.contains("float")) {
            return Float.class;
        } else if (t.contains("double")) {
            return Double.class;
        } else if (t.contains("json") || t.contains("enum")) {
            return String.class;
        } else if (t.contains("boolean")) {
            return Boolean.class;
        } else if (t.contains("numeric")) {
            return java.math.BigDecimal.class;
        }
        return String.class;
    }
}