package org.test4j.module.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ColumnDef: 字段定义
 *
 * @author darui.wu
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnDef {
    /**
     * 字段名称
     *
     * @return
     */
    String value() default "";

    /**
     * 数据库字段类型
     *
     * @return
     */
    String type();

    /**
     * 是否主键
     *
     * @return
     */
    boolean primary() default false;

    /**
     * 是否自增
     *
     * @return
     */
    boolean autoIncrease() default false;

    /**
     * 允许字段为null
     *
     * @return
     */
    boolean notNull() default false;

    /**
     * 默认值
     *
     * @return
     */
    String defaultValue() default "";
}