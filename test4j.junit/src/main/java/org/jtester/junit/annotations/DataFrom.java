package org.jtester.junit.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ METHOD })
@SuppressWarnings("rawtypes")
public @interface DataFrom {
	String value() default "";

	Class clazz() default DataFrom.class;

	DataSource source() default DataSource.FromMethod;

	public static enum DataSource {
		/**
		 * 数据由类的静态方法生产
		 */
		FromMethod,
		/**
		 * 数据从文件中读入
		 */
		FromFile;
	}
}
