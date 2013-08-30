package org.jtester.hamcrest;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class TestBooleanAssert extends JTester {

	@Test
	public void test1() {
		want.bool(true).isEqualTo(true);
		want.bool(true).is(true);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void test2() {
		want.bool(true).is(false);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void test3() {
		// fail();
		want.fail();
	}
}
