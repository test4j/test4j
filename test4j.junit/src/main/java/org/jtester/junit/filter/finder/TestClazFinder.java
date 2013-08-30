package org.jtester.junit.filter.finder;

import java.util.List;

/**
 * 测试类查找器
 * 
 * @author darui.wudr
 * 
 */
public interface TestClazFinder {
	List<Class<?>> find();
}