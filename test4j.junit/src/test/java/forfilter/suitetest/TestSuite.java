package forfilter.suitetest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import forfilter.tests.p1.P1Test;
import forfilter.tests.p2.ConcreteP2Test;

@RunWith(Suite.class)
@SuiteClasses({ P1Test.class, ConcreteP2Test.class })
public class TestSuite {

}
