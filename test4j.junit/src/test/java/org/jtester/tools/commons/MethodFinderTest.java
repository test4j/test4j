package org.jtester.tools.commons;

import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.junit.JTester;
import org.junit.Ignore;
import org.junit.Test;

/**
 * this test is broken for EclEmma Test
 * 
 * @author darui.wudr
 */
public class MethodFinderTest implements JTester {
    @Test
    @Ignore
    public void findTestMethod_1() {
        List<String> methods = MethodFinder.findTestMethod(User.class, "findAddress");
        want.collection(methods).sizeEq(1);
        want.collection(methods).hasAllItems("org.jtester.fortest.hibernate.UserServiceImpl.findAddress");
    }

    @Test
    @Ignore
    public void findTestMethod_2() {
        List<String> methods = MethodFinder.findTestMethod(User.class, "getUser");
        want.collection(methods).sizeEq(1);
        want.collection(methods).hasAllItems("org.jtester.fortest.hibernate.UserServiceImpl.getUser");
    }
}
