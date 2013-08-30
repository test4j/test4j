package org.jtester.module.spring.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@SuppressWarnings("rawtypes")
@Retention(RUNTIME)
public @interface Property {
	/**
	 * 属性名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * 属性值
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * ref bean名称
	 * 
	 * @return
	 */
	String ref() default "";

	/**
	 * ref bean的class实现类
	 * 
	 * @return
	 */
	Class clazz() default Property.class;
}
