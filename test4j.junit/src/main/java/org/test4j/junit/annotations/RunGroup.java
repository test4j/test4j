package org.test4j.junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 根据定义在测试类或测试方法上的 @Group 注解定义的组执行测试
 * 
 * @author darui.wudr 2015年4月10日 下午5:16:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RunGroup {
    /**
     * 要被运行的组
     * 
     * @return
     */
    String[] includes() default {};

    /**
     * 排除运行的组
     * 
     * @return
     */
    String[] excludes() default {};
}
