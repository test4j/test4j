package forfilter.samples;

import static org.test4j.junit.filter.SuiteType.JUNIT38_TEST_CLASSES;
import static org.test4j.junit.filter.SuiteType.JUNT4_TEST_CLASSES;

import org.junit.runner.RunWith;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.annotations.ClazFinder.BaseType;
import org.test4j.junit.suitetest.suite.ClassPathSuite;

import forfilter.tests.p2.AbstractP2Test;

@RunWith(ClassPathSuite.class)
@ClazFinder(value = { JUNT4_TEST_CLASSES, JUNIT38_TEST_CLASSES }, baseType = @BaseType(includes = AbstractP2Test.class))
public class ByBasetypeFilterSuite {
}
