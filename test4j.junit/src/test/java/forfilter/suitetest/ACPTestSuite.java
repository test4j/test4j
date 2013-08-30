package forfilter.suitetest;

import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClazFinder(patterns = "oops")
public class ACPTestSuite {
}
