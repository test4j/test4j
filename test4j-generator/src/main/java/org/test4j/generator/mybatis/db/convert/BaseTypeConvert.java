package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.ColumnJavaType;
import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.ITypeConvert;

/**
 * BaseTypeConvert
 *
 */
public abstract class BaseTypeConvert implements ITypeConvert {

    protected ColumnJavaType parseDateType(DateType dateType, String columnType) {
        switch (dateType) {
            case SQL_PACK:
                switch (columnType) {
                    case "date":
                    case "year":
                        return ColumnJavaType.DATE_SQL;
                    case "time":
                        return ColumnJavaType.TIME;
                    default:
                        return ColumnJavaType.TIMESTAMP;
                }
            case TIME_PACK:
                switch (columnType) {
                    case "date":
                        return ColumnJavaType.LOCAL_DATE;
                    case "time":
                        return ColumnJavaType.LOCAL_TIME;
                    case "year":
                        return ColumnJavaType.YEAR;
                    default:
                        return ColumnJavaType.LOCAL_DATE_TIME;
                }
            case ONLY_DATE:
            default:
                return ColumnJavaType.DATE;
        }
    }
}