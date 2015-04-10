package forfilter.samples;

import static org.test4j.junit.filter.SuiteType.SUITE_TEST_CLASSES;

import org.junit.runner.RunWith;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suitetest.suite.ClasspathSuite;

/**
 * Run all test suites in this package except itself (to prevent JUnit's
 * recursion exception)
 */

@RunWith(ClasspathSuite.class)
@ClazFinder(patterns = { "samples.*", "!samples\\.ANestingCpSuite" }, value = { SUITE_TEST_CLASSES })
public class ANestingCpSuite {

}
