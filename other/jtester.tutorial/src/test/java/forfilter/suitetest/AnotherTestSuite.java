package forfilter.suitetest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ injar.p1.P1Test.class, injar.p2.P2Test.class })
public class AnotherTestSuite {

}
