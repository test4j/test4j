package org.test4j.junit.suitetest;

import org.junit.experimental.categories.Categories.IncludeCategory;
import org.test4j.junit.suitetest.CategorySuiteTest;

import forfilter.DaveyCategory;

@IncludeCategory(DaveyCategory.class)
public class CategoryTestTest extends CategorySuiteTest {
}
