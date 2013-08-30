package forfilter.samples;

import static org.jtester.junit.filter.SuiteType.JUNIT38_TEST_CLASSES;
import static org.jtester.junit.filter.SuiteType.JUNT4_TEST_CLASSES;

import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.annotations.ClazFinder.BaseType;
import org.jtester.junit.suite.ClasspathSuite;
import org.junit.runner.RunWith;

import forfilter.tests.p2.AbstractP2Test;

@RunWith(ClasspathSuite.class)
@ClazFinder(value = { JUNT4_TEST_CLASSES, JUNIT38_TEST_CLASSES }, baseType = @BaseType(includes = AbstractP2Test.class))
public class ByBasetypeFilterSuite {
}
