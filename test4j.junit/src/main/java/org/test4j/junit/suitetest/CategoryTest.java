package org.test4j.junit.suitetest;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 根据 @IncludeCategory 和 @ExcludeCategory进行分组测试的suite基类
 * 
 * @author darui.wudr
 */
@RunWith(Categories.class)
@Suite.SuiteClasses({ ClasspathTest.class })
public abstract class CategoryTest {
}
