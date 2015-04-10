package forfilter.samples;

import static org.test4j.junit.filter.SuiteType.JUNIT38_TEST_CLASSES;

import org.junit.runner.RunWith;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suitetest.suite.ClasspathSuite;

@RunWith(ClasspathSuite.class)
@ClazFinder(patterns = { "tests.*" }, value = { JUNIT38_TEST_CLASSES })
public class AllJUnit38Tests {

}
