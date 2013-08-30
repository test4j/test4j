package org.jtester.testng;

import java.io.File;

import org.jtester.testng.JTester;
import org.jtester.testng.report.UserTestReporter;
import org.jtester.testng.testcase.ChildTestCase1;
import org.jtester.testng.testcase.ChildTestCase2;
import org.jtester.testng.testcase.ParentTestCase;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class UserTestReporterTest extends JTester {
	@Test
	//@Transactional(TransactionMode.DISABLED)
	public void testOnFinish() {
		String filepath = System.getProperty("user.dir") + "/target/UserTestMethods.html";
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		TestListenerAdapter report = new UserTestReporter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { ParentTestCase.class, ChildTestCase1.class, ChildTestCase2.class });
		testng.addListener(report);
		testng.run();

		want.file(filepath).isExists();
	}
}
