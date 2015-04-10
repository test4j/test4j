import org.test4j.junit.annotations.TestPath;
import org.test4j.junit.suitetest.ClassPathSuiteTest;

@TestPath(patterns = { "org.test4j.*", "ext.test4j.*" })
public class AllJunit4Test extends ClassPathSuiteTest {

}
