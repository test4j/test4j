package org.test4j.testng;

import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "debug")
public class Test4JTest_TimeOut extends Test4J {
	@Test
	public void test() throws InterruptedException {
		Thread.sleep(15000);
	}

	@Test
	public void test2() throws InterruptedException {
		Thread.sleep(100);
	}
}
