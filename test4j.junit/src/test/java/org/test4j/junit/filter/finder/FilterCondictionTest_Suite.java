package org.test4j.junit.filter.finder;

import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suitetest.ClassPathSuiteTest;

@ClazFinder(patterns = { "org.test4j.junit.filter.finder.*", "!*ClasspathTestClazFinderTest" })
public class FilterCondictionTest_Suite extends ClassPathSuiteTest {

}
