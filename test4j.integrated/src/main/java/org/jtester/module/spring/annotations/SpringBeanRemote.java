package org.jtester.module.spring.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用来定义SpringHttpInvoker client bean
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
@Target(FIELD)
@Retention(RUNTIME)
public @interface SpringBeanRemote {
	/**
	 * httpInvoker的 beanid
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * httpInvoke的serviceUrl
	 * 
	 * @return
	 */
	String serviceUrl() default "";

	/**
	 * httpInvoker的serviceInterface
	 * 
	 * @return
	 */
	Class serviceInterface() default Object.class;

	/**
	 * 远程连接方式
	 * 
	 * @return
	 */
	SpringBeanRemoteType type() default SpringBeanRemoteType.hessian;

	public static enum SpringBeanRemoteType {
		hessian, httpinvoker;// esb?

		/**
		 * 从url路径中解析出remote bean的类型
		 */
		public final static SpringBeanRemoteType getTypeFromURL(String url) {
			if (url == null || url.contains("/") == false) {
				return null;
			}
			if (url.startsWith(hessian.name() + "/")) {
				return hessian;
			}
			if (url.startsWith(httpinvoker.name() + "/")) {
				return httpinvoker;
			}
			return null;
		}
	}
}
