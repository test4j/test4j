import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suite.AllTestSuite;

@ClazFinder(patterns = { "org.test4j.*", "ext.test4j.*" })
public class AllJunit4Test extends AllTestSuite {

}
