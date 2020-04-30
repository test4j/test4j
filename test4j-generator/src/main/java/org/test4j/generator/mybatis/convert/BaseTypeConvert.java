package org.test4j.generator.mybatis.convert;

import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.test4j.generator.mybatis.rule.DateType;
import org.test4j.generator.mybatis.rule.IColumnType;

/**
 * BaseTypeConvert
 *
 * @author wudarui
 */
public abstract class BaseTypeConvert implements ITypeConvert {

    protected IColumnType parseDateType(DateType dateType, String columnType) {
        switch (dateType) {
            case SQL_PACK:
                switch (columnType) {
                    case "date":
                    case "year":
                        return ColumnType.DATE_SQL;
                    case "time":
                        return ColumnType.TIME;
                    default:
                        return ColumnType.TIMESTAMP;
                }
            case TIME_PACK:
                switch (columnType) {
                    case "date":
                        return ColumnType.LOCAL_DATE;
                    case "time":
                        return ColumnType.LOCAL_TIME;
                    case "year":
                        return ColumnType.YEAR;
                    default:
                        return ColumnType.LOCAL_DATE_TIME;
                }
            case ONLY_DATE:
            default:
                return ColumnType.DATE;
        }
    }
}
