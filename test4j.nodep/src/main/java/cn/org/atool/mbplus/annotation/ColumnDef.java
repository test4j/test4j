package cn.org.atool.mbplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnDef {
    /**
     * 数据库字段类型
     *
     * @return 字段类型
     */
    String type();

    /**
     * 是否主键
     *
     * @return 是否主键
     */
    boolean primary() default false;

    /**
     * 允许字段为null
     *
     * @return 是否为空
     */
    boolean notNull() default false;
}
