package forfilter.samples;

import static org.jtester.junit.filter.SuiteType.SUITE_TEST_CLASSES;
import static org.jtester.junit.filter.SuiteType.JUNT4_TEST_CLASSES;

import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClazFinder(patterns = { "suitetest.*", "tests\\.p[12].*" }, value = { SUITE_TEST_CLASSES, JUNT4_TEST_CLASSES })
public class ComplexSuite {

}
