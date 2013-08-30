package org.jtester.junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jtester.junit.filter.SuiteType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClazFinder {
	/**
	 * The <code>SuiteTypes</code> annotation specifies which types of tests
	 * will be included in the test run. You can choose one or more from
	 * TEST_CLASSES, RUN_WITH_CLASSES, JUNIT38_TEST_CLASSES. If none is
	 * specified only JUnit4-style TEST_CLASSES will be run.<br>
	 * 返回测试类的类型
	 */
	public SuiteType[] value() default { SuiteType.JUNT4_TEST_CLASSES };

	/**
	 * The <code>jars</code> method specifies if Jars should be searched in or
	 * not. If the annotation is missing Jars are not being searched.<br>
	 * 是否查找jar包中的测试类
	 * 
	 * @return
	 */
	public boolean inJars() default false;

	/**
	 * The <code>filters</code> method specifies a set of regex expressions for
	 * all test classes (ie. their qualified names) to include in the test run.
	 * When the annotation is missing, all test classes in all packages will be
	 * run.<br>
	 * 返回测试类过滤规则
	 */
	public String[] patterns() default {};

	/**
	 * 根据基类查找测试类
	 * 
	 * @return
	 */
	public BaseType baseType() default @BaseType;

	public static @interface BaseType {
		/**
		 * The <code>baseTypes</code> method filters all test classes to be run
		 * by one or several given base types, i.e. only those classes will be
		 * run which extend one of the base types. Default is
		 * <code>Object.class</code>.<br>
		 * 运行继承指定基类的测试
		 */
		Class<?>[] includes() default { Object.class };

		/**
		 * The <code>excludedBaseTypes</code> method filters all test classes to
		 * be run by one or several given base types, i.e. only those classes
		 * will be run which <em>do not extend</em> any of the base types.
		 * Default is <code>Object.class</code>.<br>
		 * 不运行继承指定基类的测试
		 */
		Class<?>[] excludes() default {};
	}
}
