package org.jtester.testng.testcase;

import org.testng.annotations.Test;

/**
 * Comment of TestCase2
 * 
 * @author zili.dengzl
 * 
 */
@Test(groups = { "child-test", "for-test" })
public class ChildTestCase1 extends ParentTestCase {
	public void test_success_1() throws InterruptedException {
	}

	public void test_success_2() {
	}

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
