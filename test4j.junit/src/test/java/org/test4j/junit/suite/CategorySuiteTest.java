package org.test4j.junit.suite;

import org.junit.experimental.categories.Categories.IncludeCategory;
import org.test4j.junit.suite.CategorySuite;

import forfilter.DaveyCategory;

@IncludeCategory(DaveyCategory.class)
public class CategorySuiteTest extends CategorySuite {
}
