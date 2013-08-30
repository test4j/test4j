package org.test4j.tools.commons;

import java.util.List;

import org.test4j.fortest.beans.User;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

/**
 * this test is broken for EclEmma Test
 * 
 * @author darui.wudr
 */
@Test(groups = { "broken" })
public class MethodFinderTest extends Test4J {
    public void findTestMethod_1() {
        List<String> methods = MethodFinder.findTestMethod(User.class, "findAddress");
        want.collection(methods).sizeEq(1);
        want.collection(methods).hasAllItems("org.test4j.fortest.hibernate.UserServiceImpl.findAddress");
    }

    public void findTestMethod_2() {
        List<String> methods = MethodFinder.findTestMethod(User.class, "getUser");
        want.collection(methods).sizeEq(1);
        want.collection(methods).hasAllItems("org.test4j.fortest.hibernate.UserServiceImpl.getUser");
    }
}
