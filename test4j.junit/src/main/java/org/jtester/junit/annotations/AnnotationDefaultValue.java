package org.jtester.junit.annotations;

import org.jtester.junit.filter.SuiteType;

/**
 * 注解函数中的默认值
 * 
 * @author darui.wudr
 * 
 */
public interface AnnotationDefaultValue {
	/**
	 * 默认不包含jar包中的测试类
	 */
	public static final boolean DEFAULT_INCLUDE_JARS = false;

	/**
	 * 默认测试类过滤规则为空
	 */
	public static final String[] DEFAULT_CLASSNAME_FILTERS = new String[] {};

	/**
	 * 默认只执行junit4测试
	 */
	public static final SuiteType[] DEFAULT_SUITE_TYPES = new SuiteType[] { SuiteType.JUNT4_TEST_CLASSES };
	/**
	 * 默认继承基类是 Object
	 */
	public static final Class<?>[] DEFAULT_INCLUDED_BASE_TYPES = new Class<?>[] { Object.class };
	/**
	 * 默认不排除任何基类
	 */
	public static final Class<?>[] DEFAULT_EXCLUDED_BASES_TYPES = new Class<?>[] {};

	public static final String DEFAULT_CLASSPATH_PROPERTY = "java.class.path";

	/**
	 * 需要被运行和排除的组的默认值
	 */
	public static final String[] DEFAULT_GROUP_VALUE = new String[] {};
}
