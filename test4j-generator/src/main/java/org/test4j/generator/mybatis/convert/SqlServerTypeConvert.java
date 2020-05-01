package org.test4j.generator.mybatis.convert;

import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.test4j.generator.mybatis.rule.DateType;
import org.test4j.generator.mybatis.rule.IColumnType;

/**
 * SQLServer 字段类型转换
 *
 * @author wudarui
 */
public class SqlServerTypeConvert extends BaseTypeConvert {

    @Override
    public IColumnType processTypeConvert(DateType dateType, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char") || t.contains("xml")) {
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
        } else if (t.contains("decimal") || t.contains("numeric")) {
            return ColumnType.DOUBLE;
        } else if (t.contains("money")) {
            return ColumnType.BIG_DECIMAL;
        } else if (t.contains("binary") || t.contains("image")) {
            return ColumnType.BYTE_ARRAY;
        } else if (t.contains("float") || t.contains("real")) {
            return ColumnType.FLOAT;
        }
        return ColumnType.STRING;
    }
}
