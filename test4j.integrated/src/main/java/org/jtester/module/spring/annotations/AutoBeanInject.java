package org.jtester.module.spring.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 是否自动往spring容器配置bean的定义
 * 
 * @author darui.wudr
 * 
 */
@Target({ TYPE })
@Retention(RUNTIME)
public @interface AutoBeanInject {
	/**
	 * enable auto config
	 * 
	 * @return
	 */
	boolean value() default true;

	/**
	 * 接口类和实现类的映射关系
	 * 
	 * @return
	 */
	BeanMap[] maps() default {};

	/**
	 * 显式排除下列属性的注入
	 * 
	 * @return
	 */
	String[] excludeProperties() default {};

	/**
	 * 显式排除下列package属性的注入
	 * 
	 * @return
	 */
	String[] excludePackages() default {};

	/**
	 * 忽略找不到的实现的属性注入
	 * 
	 * @return
	 */
	boolean ignoreNotFound() default true;

	@Retention(RUNTIME)
	public static @interface BeanMap {
		/**
		 * 接口类表达式
		 * 
		 * @return
		 */
		String intf();

		/**
		 * 实现类表达式
		 * 
		 * @return
		 */
		String impl();
	}
}
