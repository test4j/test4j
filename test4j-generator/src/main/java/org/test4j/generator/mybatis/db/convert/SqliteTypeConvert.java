package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.DateType;

/**
 * SQLite 字段类型转换
 */
public class SqliteTypeConvert extends BaseTypeConvert {
    @Override
    public Class processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("bigint")) {
            return Long.class;
        } else if (t.contains("tinyint(1)") || t.contains("boolean")) {
            return Boolean.class;
        } else if (t.contains("int")) {
            return Integer.class;
        } else if (t.contains("text") || t.contains("char") || t.contains("enum")) {
            return String.class;
        } else if (t.contains("decimal") || t.contains("numeric")) {
            return java.math.BigDecimal.class;
        } else if (t.contains("clob")) {
            return java.sql.Clob.class;
        } else if (t.contains("blob")) {
            return java.sql.Blob.class;
        } else if (t.contains("float")) {
            return Float.class;
        } else if (t.contains("double")) {
            return Double.class;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return this.parseDateType(dateType, t);
        }
        return String.class;
    }
}