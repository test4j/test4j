import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suite.AllTestSuite;

@ClazFinder(patterns = { "org.test4j.spec.*" })
public class AllJSpecTest extends AllTestSuite {

}
