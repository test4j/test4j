package org.test4j.generator.mybatis.convert;

import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.model.BuildConfig;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.test4j.generator.mybatis.rule.DateType;
import org.test4j.generator.mybatis.rule.IColumnType;

/**
 * MYSQL 数据库字段类型转换
 *
 * @author wudarui
 */
public class MySqlTypeConvert extends BaseTypeConvert {

    @Override
    public IColumnType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return ColumnType.STRING;
        } else if (t.contains("bigint")) {
            return ColumnType.LONG;
        } else if (t.contains("tinyint(1)")) {
            return ColumnType.BOOLEAN;
        } else if (t.contains("int")) {
            return ColumnType.INTEGER;
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
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return this.parseDateType(dateType, t);
        }
        return ColumnType.STRING;
    }
}
