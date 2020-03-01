package org.test4j.module.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author darui.wu
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnDef {
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
    cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType primary() default cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType.None;

    /**
     * 允许字段为null
     *
     * @return
     */
    boolean notNull() default false;

    enum PrimaryType {
        /**
         * 非主键
         */
        None,
        /**
         * 自增主键
         */
        AutoIncrease,
        /**
         * 自定义
         */
        Customized;
    }
}
