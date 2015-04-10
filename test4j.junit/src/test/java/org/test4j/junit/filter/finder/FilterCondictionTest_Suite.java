package org.test4j.junit.filter.finder;

import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suitetest.Test4JSuite;

@ClazFinder(patterns = { "org.test4j.junit.filter.finder.*", "!*ClasspathTestClazFinderTest" })
public class FilterCondictionTest_Suite extends Test4JSuite {

}
