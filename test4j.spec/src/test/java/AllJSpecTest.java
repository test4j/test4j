import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.AllTestSuite;

@ClazFinder(patterns = { "org.jtester.spec.*" })
public class AllJSpecTest extends AllTestSuite {

}
