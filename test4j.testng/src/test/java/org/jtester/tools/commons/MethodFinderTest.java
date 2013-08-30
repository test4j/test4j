package org.jtester.tools.commons;

import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.MethodFinder;
import org.testng.annotations.Test;

/**
 * this test is broken for EclEmma Test
 * 
 * @author darui.wudr
 * 
 */
@Test(groups = { "broken" })
public class MethodFinderTest extends JTester {
	public void findTestMethod_1() {
		List<String> methods = MethodFinder.findTestMethod(User.class, "findAddress");
		want.collection(methods).sizeEq(1);
		want.collection(methods).hasAllItems("org.jtester.fortest.hibernate.UserServiceImpl.findAddress");
	}

	public void findTestMethod_2() {
		List<String> methods = MethodFinder.findTestMethod(User.class, "getUser");
		want.collection(methods).sizeEq(1);
		want.collection(methods).hasAllItems("org.jtester.fortest.hibernate.UserServiceImpl.getUser");
	}
}
