package org.jtester.testng.testcase;

import org.testng.annotations.Test;

public class ChildTestCase2 extends ParentTestCase {
	public void test_success_3() {
	}

	@Test(groups = "MethodTest")
	public void test_success_4() {
	}

	public void test_failed_1() {
		want.bool(true).isEqualTo(false);
	}

	@Test(dependsOnMethods = "test_failed_1")
	public void test_skip_1() {
	}

}
