package org.test4j.generator.mybatis.db.convert;

import org.test4j.generator.mybatis.db.DateType;
import org.test4j.generator.mybatis.db.ITypeConvert;

/**
 * BaseTypeConvert
 *
 */
public abstract class BaseTypeConvert implements ITypeConvert {

    protected Class parseDateType(DateType dateType, String columnType) {
        switch (dateType) {
            case SQL_PACK:
                switch (columnType) {
                    case "date":
                    case "year":
                        return java.sql.Date.class;
                    case "time":
                        return java.sql.Time.class;
                    default:
                        return java.sql.Timestamp.class;
                }
            case TIME_PACK:
                switch (columnType) {
                    case "date":
                        return java.time.LocalDate.class;
                    case "time":
                        return java.time.LocalTime.class;
                    case "year":
                        return java.time.Year.class;
                    default:
                        return java.time.LocalDateTime.class;
                }
            case ONLY_DATE:
            default:
                return java.util.Date.class;
        }
    }
}