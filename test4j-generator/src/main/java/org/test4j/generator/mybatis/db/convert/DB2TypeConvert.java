package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.ColumnJavaType;
import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.ITypeConvert;

/**
 * DB2 字段类型转换
 *
 */
public class DB2TypeConvert implements ITypeConvert {

    @Override
    public ColumnJavaType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return ColumnJavaType.STRING;
        } else if (t.contains("bigint")) {
            return ColumnJavaType.LONG;
        } else if (t.contains("smallint")) {
            return ColumnJavaType.BASE_SHORT;
        } else if (t.contains("int")) {
            return ColumnJavaType.INTEGER;
        } else if (t.contains("date") || t.contains("time") || t.contains("year") || t.contains("timestamp")) {
            return ColumnJavaType.DATE;
        } else if (t.contains("text")) {
            return ColumnJavaType.STRING;
        } else if (t.contains("bit")) {
            return ColumnJavaType.BOOLEAN;
        } else if (t.contains("decimal")) {
            return ColumnJavaType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return ColumnJavaType.CLOB;
        } else if (t.contains("blob")) {
            return ColumnJavaType.BLOB;
        } else if (t.contains("binary")) {
            return ColumnJavaType.BYTE_ARRAY;
        } else if (t.contains("float")) {
            return ColumnJavaType.FLOAT;
        } else if (t.contains("double")) {
            return ColumnJavaType.DOUBLE;
        } else if (t.contains("json") || t.contains("enum")) {
            return ColumnJavaType.STRING;
        }
        return ColumnJavaType.STRING;
    }

}