package org.jtester.module.spring.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD, METHOD })
@Retention(RUNTIME)
@SuppressWarnings("rawtypes")
public @interface SpringBeanByType {
	/**
	 * 显式的指定spring bean主动注册时要实现的实现类class<br>
	 * 
	 * @return
	 */
	Class value() default AutoBeanInject.class;

	/**
	 * spring bean配置中的init-method方法<br>
	 * 
	 * @return
	 */
	String init() default "";

	/**
	 * 定义bean的简单属性值，和别名引用bean的情形
	 * 
	 * @return
	 */
	Property[] properties() default {};
}
