package forfilter.samples;

import static org.jtester.junit.filter.SuiteType.SUITE_TEST_CLASSES;

import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClazFinder(patterns = { "suitetest.*TestSuite" }, value = { SUITE_TEST_CLASSES })
public class AllRunWithSuites {

}
