package org.jtester.hamcrest.iassert.common.impl;

import java.util.HashMap;
import java.util.Map;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.junit.JTester;
import org.junit.Test;

public class BaseAssertTest implements JTester {

    @Test(expected = AssertionError.class)
    public void testClazIs() {
        want.map(new HashMap<String, Object>()).clazIs(String.class);
    }

    @Test
    public void testClazIs2() {
        want.map(new HashMap<String, Object>()).clazIs(Map.class);
    }

    @Test
    public void testAllOf() {
        want.string("test1").all(the.string().contains("test"), the.string().regular("\\w{5}"));
    }

    @Test
    public void testAllOf_iterable() {
        want.string("test1").all(the.string().contains("test"), the.string().regular("\\w{5}"));
    }

    @Test
    public void testAnyOf() {
        want.string("test1").any(the.string().contains("test4"), the.string().regular("\\w{5}"));
        want.string("test1").any(the.string().contains("test1"), the.string().regular("\\w{6}"));
    }

    @Test(expected = AssertionError.class)
    public void testAnyOf_failure() {
        want.string("test1").any(the.string().contains("test4"), the.string().regular("\\w{6}"));
    }

    @Test
    public void testAnyOf_iterable() {
        want.string("test1").any(the.string().contains("test"), the.string().regular("\\w{6}"));
        want.string("test1").any(the.string().contains("test5"), the.string().regular("\\w{5}"));
    }

    @Test(expected = AssertionError.class)
    public void testAnyOf_iterable_failure() {
        want.string("test1").any(the.string().contains("test6"), the.string().regular("\\w{6}"));
    }

    @Test
    public void testEqToString() {
        User user = User.newInstance(124, "darui.wu");
        want.object(user).eqToString("User [id=124, name=darui.wu]");
    }

    @Test
    public void testMatchToString() {
        User user = User.newInstance(124, "darui.wu");
        want.object(user).eqToString(the.string().eq("User[id=124,name=darui.wu]", StringMode.IgnoreSpace));
    }

    @Test
    public void testNotAny() {
        String actual = "I am a test.";
        want.string(actual).notAny(the.string().isEqualTo("bcd"), the.string().contains("bce"));
    }

    @Test(expected = AssertionError.class)
    public void testNotAny_Failure() {
        String actual = "I am a test.";
        want.string(actual).notAny(the.string().isEqualTo("bcd"), the.string().contains("test"));
    }

    @Test
    public void testNotAll() {
        String actual = "I am a test.";
        want.string(actual).notAll(the.string().contains("testedObject"), the.string().contains("java"));
    }

    @Test(expected = AssertionError.class)
    public void testNotAll_Failure() {
        String actual = "I am a java test.";
        want.string(actual).notAll(the.string().contains("test"), the.string().contains("java"));
    }
}
