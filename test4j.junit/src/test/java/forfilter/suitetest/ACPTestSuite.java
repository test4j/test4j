package forfilter.suitetest;

import org.junit.runner.RunWith;
import org.test4j.junit.annotations.TestPath;
import org.test4j.junit.suitetest.suite.ClassPathSuite;

@RunWith(ClassPathSuite.class)
@TestPath(patterns = "oops")
public class ACPTestSuite {
}
