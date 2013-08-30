package org.test4j.tools.commons;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.test4j.fortest.beans.User;
import org.test4j.junit.JTester;
import org.test4j.tools.commons.MethodFinder;

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
