package forfilter.samples;

import static org.jtester.junit.filter.SuiteType.JUNIT38_TEST_CLASSES;

import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClazFinder(patterns = { "tests.*" }, value = { JUNIT38_TEST_CLASSES })
public class AllJUnit38Tests {

}
