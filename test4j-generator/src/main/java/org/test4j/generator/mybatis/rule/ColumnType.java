package org.test4j.generator.mybatis.rule;

import lombok.Getter;
import org.test4j.generator.mybatis.model.IJavaType;

/**
 * 表字段类型
 *
 * @author wudarui
 */
public enum ColumnType implements IJavaType {
    // 基本类型
    BASE_BYTE(byte.class.getSimpleName()),
    BASE_SHORT(short.class.getSimpleName()),
    BASE_CHAR(char.class.getSimpleName()),
    BASE_INT(int.class.getSimpleName()),
    BASE_LONG(long.class.getSimpleName()),
    BASE_FLOAT(float.class.getSimpleName()),
    BASE_DOUBLE(double.class.getSimpleName()),
    BASE_BOOLEAN(boolean.class.getSimpleName()),

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
    BYTE_ARRAY(byte[].class.getSimpleName()),
    OBJECT(Object.class.getSimpleName()),
    DATE(java.util.Date.class),
    BIG_INTEGER(java.math.BigInteger.class),
    BIG_DECIMAL(java.math.BigDecimal.class);

    /**
     * 类型
     */
    @Getter
    private final String fieldType;

    /**
     * 包路径
     */
    @Getter
    private final String importName;

    ColumnType(Class type) {
        this(type.getSimpleName(), type.getName());
    }

    ColumnType(String type) {
        this(type, null);
    }

    ColumnType(final String type, final String fullName) {
        this.fieldType = type;
        this.importName = fullName;
    }
}
