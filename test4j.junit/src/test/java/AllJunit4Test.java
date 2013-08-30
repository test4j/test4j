import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.AllTestSuite;

@ClazFinder(patterns = { "org.jtester.*", "ext.jtester.*" })
public class AllJunit4Test extends AllTestSuite {

}
