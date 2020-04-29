package org.test4j.generator.mybatis.rule;

import lombok.Getter;

/**
 * 表字段类型
 */
public enum DbColumnType implements IColumnType {
    // 基本类型
    BASE_BYTE("byte"),
    BASE_SHORT("short"),
    BASE_CHAR("char"),
    BASE_INT("int"),
    BASE_LONG("long"),
    BASE_FLOAT("float"),
    BASE_DOUBLE("double"),
    BASE_BOOLEAN("boolean"),

    // 包装类型
    BYTE(Byte.class.getSimpleName()),
    SHORT(Short.class.getSimpleName()),
    CHARACTER(Character.class.getSimpleName()),
    INTEGER(Integer.class.getSimpleName()),
    LONG(Long.class.getSimpleName()),
    FLOAT(Float.class.getSimpleName()),
    DOUBLE(Double.class.getSimpleName()),
    BOOLEAN(Boolean.class.getSimpleName()),
    STRING(String.class.getSimpleName()),

    // sql 包下数据类型
    DATE_SQL(java.sql.Date.class),
    TIME(java.sql.Time.class),
    TIMESTAMP(java.sql.Timestamp.class),
    BLOB(java.sql.Blob.class),
    CLOB(java.sql.Clob.class),

    // java8 新时间类型
    LOCAL_DATE(java.time.LocalDate.class),
    LOCAL_TIME(java.time.LocalTime.class),
    YEAR(java.time.Year.class),
    YEAR_MONTH(java.time.YearMonth.class),
    LOCAL_DATE_TIME(java.time.LocalDateTime.class),
    INSTANT(java.time.Instant.class),

    // 其他杂类
    BYTE_ARRAY("byte[]"),
    OBJECT(Object.class.getSimpleName()),
    DATE(java.util.Date.class),
    BIG_INTEGER(java.math.BigInteger.class),
    BIG_DECIMAL(java.math.BigDecimal.class);

    /**
     * 类型
     */
    @Getter
    private final String propertyType;

    /**
     * 包路径
     */
    @Getter
    private final String importPackage;

    DbColumnType(Class type) {
        this.propertyType = type.getSimpleName();
        this.importPackage = type.getName();
    }

    DbColumnType(String type) {
        this(type, null);
    }

    DbColumnType(final String type, final String pkg) {
        this.propertyType = type;
        this.importPackage = pkg;
    }
}
