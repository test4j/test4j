package forfilter.samples;

import static org.test4j.junit.filter.SuiteType.JUNT4_TEST_CLASSES;
import static org.test4j.junit.filter.SuiteType.SUITE_TEST_CLASSES;

import org.junit.runner.RunWith;
import org.test4j.junit.annotations.TestPath;
import org.test4j.junit.suitetest.suite.ClassPathSuite;

@RunWith(ClassPathSuite.class)
@TestPath(patterns = { "suitetest.*", "tests\\.p[12].*" }, value = { SUITE_TEST_CLASSES, JUNT4_TEST_CLASSES })
public class ComplexSuite {

}
