package org.test4j.generator.db.convert;

import org.test4j.generator.db.DateType;

/**
 * MYSQL 数据库字段类型转换
 *
 */
public class MySqlTypeConvert extends BaseTypeConvert {

    @Override
    public Class processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return String.class;
        } else if (t.contains("bigint")) {
            return Long.class;
        } else if (t.contains("tinyint(1)")) {
            return Boolean.class;
        } else if (t.contains("int")) {
            return Integer.class;
        } else if (t.contains("text")) {
            return String.class;
        } else if (t.contains("bit")) {
            return Boolean.class;
        } else if (t.contains("decimal")) {
            return java.math.BigDecimal.class;
        } else if (t.contains("clob")) {
            return java.sql.Clob.class;
        } else if (t.contains("blob")) {
            return java.sql.Blob.class;
        } else if (t.contains("binary")) {
            return byte[].class;
        } else if (t.contains("float")) {
            return Float.class;
        } else if (t.contains("double")) {
            return Double.class;
        } else if (t.contains("json") || t.contains("enum")) {
            return String.class;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return this.parseDateType(dateType, t);
        }
        return String.class;
    }
}