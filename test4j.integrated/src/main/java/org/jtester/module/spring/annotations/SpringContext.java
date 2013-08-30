package org.jtester.module.spring.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ TYPE, FIELD, METHOD })
@Retention(RUNTIME)
public @interface SpringContext {

    String[] value() default {};

    /**
     * 是否允许懒加载<br>
     * true:将所有bean加载置成懒加载模式<br>
     * false:将按照spring文件中定义的方式加载
     * 
     * @return
     */
    boolean allowLazy() default false;

    /**
     * 是否全局共享
     * 
     * @return
     */
    boolean share() default false;
}
