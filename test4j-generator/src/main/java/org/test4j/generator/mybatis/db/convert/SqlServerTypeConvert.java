package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.ColumnJavaType;
import org.test4j.generator.mybatis.db.DateType;

/**
 * SQLServer 字段类型转换
 *
 */
public class SqlServerTypeConvert extends BaseTypeConvert {

    @Override
    public ColumnJavaType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char") || t.contains("xml")) {
            return ColumnJavaType.STRING;
        } else if (t.contains("bigint")) {
            return ColumnJavaType.LONG;
        } else if (t.contains("int")) {
            return ColumnJavaType.INTEGER;
        } else if (t.contains("date") || t.contains("time")) {
            return this.parseDateType(dateType, t);
        } else if (t.contains("text")) {
            return ColumnJavaType.STRING;
        } else if (t.contains("bit")) {
            return ColumnJavaType.BOOLEAN;
        } else if (t.contains("decimal") || t.contains("numeric")) {
            return ColumnJavaType.DOUBLE;
        } else if (t.contains("money")) {
            return ColumnJavaType.BIG_DECIMAL;
        } else if (t.contains("binary") || t.contains("image")) {
            return ColumnJavaType.BYTE_ARRAY;
        } else if (t.contains("float") || t.contains("real")) {
            return ColumnJavaType.FLOAT;
        }
        return ColumnJavaType.STRING;
    }
}