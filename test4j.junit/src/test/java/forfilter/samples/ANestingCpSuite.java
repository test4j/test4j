package forfilter.samples;

import static org.jtester.junit.filter.SuiteType.SUITE_TEST_CLASSES;

import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.ClasspathSuite;
import org.junit.runner.RunWith;

/**
 * Run all test suites in this package except itself (to prevent JUnit's
 * recursion exception)
 */

@RunWith(ClasspathSuite.class)
@ClazFinder(patterns = { "samples.*", "!samples\\.ANestingCpSuite" }, value = { SUITE_TEST_CLASSES })
public class ANestingCpSuite {

}
