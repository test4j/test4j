package forfilter.samples;

import static org.jtester.junit.filter.SuiteType.JUNIT38_TEST_CLASSES;
import static org.jtester.junit.filter.SuiteType.JUNT4_TEST_CLASSES;
import junit.framework.JUnit4TestAdapter;

import org.jtester.junit.annotations.BeforeSuite;
import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.suite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClazFinder(inJars = true, patterns = { "injar.*", "tests.*" }, value = { JUNIT38_TEST_CLASSES, JUNT4_TEST_CLASSES })
public class AllTestClasses {

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllTestClasses.class);
	}

	@BeforeSuite
	public static void init1() {
		System.out.println("This is the init1 method");
	}

	@BeforeSuite
	public static void init2() {
		System.out.println("This is the init2 method");
	}
}
