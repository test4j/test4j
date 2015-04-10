import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.suitetest.ClassPathSuiteTest;

@ClazFinder(patterns = { "org.test4j.spec.*" })
public class AllJSpecTest extends ClassPathSuiteTest {

}
