package forfilter.suitetest;

import org.junit.runner.RunWith;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suitetest.suite.ClassPathSuite;

@RunWith(ClassPathSuite.class)
@ClazFinder(patterns = "oops")
public class ACPTestSuite {
}
