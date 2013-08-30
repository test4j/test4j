package org.jtester.junit.filter;

/**
 * 测试类型
 * 
 * @author darui.wudr
 */
public enum SuiteType {
	/**
	 * junit4测试
	 */
	JUNT4_TEST_CLASSES,
	/**
	 * suite类 @RunWith(Suite.class)
	 */
	SUITE_TEST_CLASSES,
	/**
	 * junit3.8测试
	 */
	JUNIT38_TEST_CLASSES
}