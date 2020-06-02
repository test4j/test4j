package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.ColumnJavaType;
import org.test4j.generator.mybatis.db.DateType;

/**
 * SQLite 字段类型转换
 *
 */
public class SqliteTypeConvert extends BaseTypeConvert {
    @Override
    public ColumnJavaType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("bigint")) {
            return ColumnJavaType.LONG;
        } else if (t.contains("tinyint(1)") || t.contains("boolean")) {
            return ColumnJavaType.BOOLEAN;
        } else if (t.contains("int")) {
            return ColumnJavaType.INTEGER;
        } else if (t.contains("text") || t.contains("char") || t.contains("enum")) {
            return ColumnJavaType.STRING;
        } else if (t.contains("decimal") || t.contains("numeric")) {
            return ColumnJavaType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return ColumnJavaType.CLOB;
        } else if (t.contains("blob")) {
            return ColumnJavaType.BLOB;
        } else if (t.contains("float")) {
            return ColumnJavaType.FLOAT;
        } else if (t.contains("double")) {
            return ColumnJavaType.DOUBLE;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return this.parseDateType(dateType, t);
        }
        return ColumnJavaType.STRING;
    }
}