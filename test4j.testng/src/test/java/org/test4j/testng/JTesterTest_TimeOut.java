package org.test4j.testng;

import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "debug")
public class JTesterTest_TimeOut extends JTester {
	@Test
	public void test() throws InterruptedException {
		Thread.sleep(15000);
	}

	@Test
	public void test2() throws InterruptedException {
		Thread.sleep(100);
	}
}
