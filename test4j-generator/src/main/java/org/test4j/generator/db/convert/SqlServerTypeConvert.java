package org.test4j.generator.db.convert;

import org.test4j.generator.db.DateType;

/**
 * SQLServer 字段类型转换
 */
public class SqlServerTypeConvert extends BaseTypeConvert {

    @Override
    public Class processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char") || t.contains("xml")) {
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
        } else if (t.contains("decimal") || t.contains("numeric")) {
            return Double.class;
        } else if (t.contains("money")) {
            return java.math.BigDecimal.class;
        } else if (t.contains("binary") || t.contains("image")) {
            return byte[].class;
        } else if (t.contains("float") || t.contains("real")) {
            return Float.class;
        }
        return String.class;
    }
}