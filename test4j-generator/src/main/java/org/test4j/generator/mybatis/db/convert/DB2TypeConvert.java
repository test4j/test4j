package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.ITypeConvert;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;

/**
 * DB2 字段类型转换
 */
public class DB2TypeConvert implements ITypeConvert {

    @Override
    public Class processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char") || t.contains("text") || t.contains("json") || t.contains("enum")) {
            return String.class;
        } else if (t.contains("bigint")) {
            return Long.class;
        } else if (t.contains("smallint") || t.contains("int")) {
            return Integer.class;
        } else if (t.contains("date") || t.contains("time") || t.contains("year") || t.contains("timestamp")) {
            return Date.class;
        } else if (t.contains("bit")) {
            return Boolean.class;
        } else if (t.contains("decimal")) {
            return BigDecimal.class;
        } else if (t.contains("clob")) {
            return Clob.class;
        } else if (t.contains("blob")) {
            return Blob.class;
        } else if (t.contains("binary")) {
            return byte[].class;
        } else if (t.contains("float")) {
            return Float.class;
        } else if (t.contains("double")) {
            return Double.class;
        } else {
            return String.class;
        }
    }
}