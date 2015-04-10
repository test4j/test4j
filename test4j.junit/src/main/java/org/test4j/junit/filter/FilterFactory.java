package org.test4j.junit.filter;

import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.filter.finder.FilterCondiction;
import org.test4j.junit.filter.finder.TestClazFinder;

public interface FilterFactory {
    /**
     * 根据 @ClazFinder 注解构造测试类查找器
     * 
     * @param clazFinder
     * @return
     */
    TestClazFinder createFinder(ClazFinder clazFinder);

    /**
     * 根据classpath系统值和过滤条件创建测试类查找器
     * 
     * @param clazzpathProp
     * @param filterCondiction
     * @return
     */
    TestClazFinder create(FilterCondiction filterCondiction);
}
