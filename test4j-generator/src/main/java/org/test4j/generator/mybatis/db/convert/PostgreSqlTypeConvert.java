package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.ColumnType;
import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.IJavaType;

/**
 * PostgreSQL 字段类型转换
 *
 */
public class PostgreSqlTypeConvert extends BaseTypeConvert {

    @Override
    public IJavaType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return ColumnType.STRING;
        } else if (t.contains("bigint")) {
            return ColumnType.LONG;
        } else if (t.contains("int")) {
            return ColumnType.INTEGER;
        } else if (t.contains("date") || t.contains("time")) {
            return this.parseDateType(dateType, t);
        } else if (t.contains("text")) {
            return ColumnType.STRING;
        } else if (t.contains("bit")) {
            return ColumnType.BOOLEAN;
        } else if (t.contains("decimal")) {
            return ColumnType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return ColumnType.CLOB;
        } else if (t.contains("blob")) {
            return ColumnType.BYTE_ARRAY;
        } else if (t.contains("float")) {
            return ColumnType.FLOAT;
        } else if (t.contains("double")) {
            return ColumnType.DOUBLE;
        } else if (t.contains("json") || t.contains("enum")) {
            return ColumnType.STRING;
        } else if (t.contains("boolean")) {
            return ColumnType.BOOLEAN;
        } else if (t.contains("numeric")) {
            return ColumnType.BIG_DECIMAL;
        }
        return ColumnType.STRING;
    }
}
