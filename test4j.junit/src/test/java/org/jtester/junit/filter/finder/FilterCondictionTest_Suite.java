package org.jtester.junit.filter.finder;

import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.AllTestSuite;

@ClazFinder(patterns = { "org.jtester.junit.filter.finder.*", "!*ClasspathTestClazFinderTest" })
public class FilterCondictionTest_Suite extends AllTestSuite {

}
