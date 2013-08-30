import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suite.AllTestSuite;

@ClazFinder(patterns = { "org.jtester.*", "ext.jtester.*" })
public class AllJunit4Test extends AllTestSuite {

}
