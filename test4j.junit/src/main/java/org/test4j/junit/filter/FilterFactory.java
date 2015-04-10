package org.test4j.junit.filter;

import java.util.Set;

import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.filter.finder.FilterCondiction;
import org.test4j.junit.filter.finder.TestClazFinder;

@SuppressWarnings("rawtypes")
public interface FilterFactory {
    /**
     * 在suite类上查找注解 @ClazFinder
     * 
     * @param suiteClazz
     * @param parents
     * @return
     */
    ClazFinder findClazFinder(Class suiteClazz, Set<Class> parents);

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
