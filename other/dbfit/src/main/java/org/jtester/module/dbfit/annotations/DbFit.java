package org.jtester.module.dbfit.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jtester.module.database.environment.DBEnvironment;

@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface DbFit {
	/**
	 * 执行dbfit文件的数据源,如果为默认值，则使用jtester的配置项
	 * 
	 * @return
	 */
	String dataSource() default DBEnvironment.DEFAULT_DATASOURCE_NAME;

	/**
	 * 单元测试前运行的wiki文件
	 * 
	 * @return
	 */
	String[] when() default {};

	/**
	 * 单元测试后校验的wiki文件
	 * 
	 * @return
	 */
	String[] then() default {};

	/**
	 * 设置fit wiki参数
	 * 
	 * @return
	 */
	FitVar[] vars() default {};

	/**
	 * 根据规则自动查找每个方法的dbfit文件
	 * 
	 * @return
	 */
	AUTO auto() default AUTO.DEFAULT;

	public static enum AUTO {
		/**
		 * 不自动查找
		 */
		UN_AUTO, // <br>
		/**
		 * 自动查找
		 */
		AUTO, // <br>

		/**
		 * 默认方式<br>
		 * o 如果class级别定义了，每个方法默认继承class中的定义方式<br>
		 * o 如果class级别没有定义，则默认是自动查找的
		 */
		DEFAULT;
	}
}
