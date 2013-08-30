package org.jtester.module.spring.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * 用在测试类中需要spring bean注入或注册的字段上<br>
 * <br>
 * This annotation can be used on fields, in order to inject a bean from a
 * spring <code>ApplicationContext</code>.<br>
 * 
 * The id of the bean in the application context is automatically derived from
 * the name of the field or the name attribute. <br>
 * 
 * 
 * An <code>ApplicationContext</code> has to be configured for this test using
 * the {@link SpringContext} annotation.
 * 
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
@SuppressWarnings("rawtypes")
public @interface SpringBeanByName {
	/**
	 * 显式的指定要注入或注册的spring bean name<br>
	 * 如果没有指定值，则name为对应字段的名称
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 显式的指定spring bean主动注册时要实现的实现类class<br>
	 * 如果指定了这个值，即使基础spring配置文件中已经有同名的bean定义<br>
	 * 也会以指定实现类的定义覆盖配置文件中的定义。
	 * 
	 * @return
	 */
	Class claz() default AutoBeanInject.class;

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
