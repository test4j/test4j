package org.jtester.junit.filter;

import org.jtester.junit.filter.finder.FilterCondiction;
import org.jtester.junit.filter.finder.TestClazFinder;

public interface FilterFactory {
	/**
	 * 根据classpath系统值和过滤条件创建测试类查找器
	 * 
	 * @param clazzpathProp
	 * @param filterCondiction
	 * @return
	 */
	public TestClazFinder create(String clazzpathProp, FilterCondiction filterCondiction);

	/**
	 * 根据suiteClass上的注解 @TestFinder 构造测试类查找器
	 * 
	 * @param suiteClazz
	 * @return
	 */
	public TestClazFinder createFinder(Class<?> suiteClazz);
}
