package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.ITypeConvert;
import org.test4j.generator.mybatis.db.ColumnType;
import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.IJavaType;

/**
 * DB2 字段类型转换
 *
 */
public class DB2TypeConvert implements ITypeConvert {

    @Override
    public IJavaType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return ColumnType.STRING;
        } else if (t.contains("bigint")) {
            return ColumnType.LONG;
        } else if (t.contains("smallint")) {
            return ColumnType.BASE_SHORT;
        } else if (t.contains("int")) {
            return ColumnType.INTEGER;
        } else if (t.contains("date") || t.contains("time") || t.contains("year") || t.contains("timestamp")) {
            return ColumnType.DATE;
        } else if (t.contains("text")) {
            return ColumnType.STRING;
        } else if (t.contains("bit")) {
            return ColumnType.BOOLEAN;
        } else if (t.contains("decimal")) {
            return ColumnType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return ColumnType.CLOB;
        } else if (t.contains("blob")) {
            return ColumnType.BLOB;
        } else if (t.contains("binary")) {
            return ColumnType.BYTE_ARRAY;
        } else if (t.contains("float")) {
            return ColumnType.FLOAT;
        } else if (t.contains("double")) {
            return ColumnType.DOUBLE;
        } else if (t.contains("json") || t.contains("enum")) {
            return ColumnType.STRING;
        }
        return ColumnType.STRING;
    }

}