package forfilter.samples;

import static org.test4j.junit.filter.SuiteType.SUITE_TEST_CLASSES;

import org.junit.runner.RunWith;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suitetest.suite.ClassPathSuite;

@RunWith(ClassPathSuite.class)
@ClazFinder(patterns = { "suitetest.*TestSuite" }, value = { SUITE_TEST_CLASSES })
public class AllRunWithSuites {

}
