package org.test4j.hamcrest.iassert.common.impl;

import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;

import java.util.HashMap;
import java.util.Map;


public class BaseAssertTest extends Test4J {

    @Test
    public void testClazIs() {
        want.exception(() ->
                        want.map(new HashMap<String, Object>()).clazIs(String.class),
                AssertionError.class);
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

    @Test
    public void testAnyOf_failure() {
        want.exception(() ->
                        want.string("test1").any(the.string().contains("test4"), the.string().regular("\\w{6}")),
                AssertionError.class);
    }

    @Test
    public void testAnyOf_iterable() {
        want.string("test1").any(the.string().contains("test"), the.string().regular("\\w{6}"));
        want.string("test1").any(the.string().contains("test5"), the.string().regular("\\w{5}"));
    }

    @Test
    public void testAnyOf_iterable_failure() {
        want.exception(() ->
                        want.string("test1").any(the.string().contains("test6"), the.string().regular("\\w{6}")),
                AssertionError.class);
    }

    @Test
    public void testEqToString() {
        User user = User.mock(124, "darui.wu");
        want.object(user).eqToString("User [id=124, name=darui.wu]");
    }

    @Test
    public void testMatchToString() {
        User user = User.mock(124, "darui.wu");
        want.object(user).eqToString(the.string().eq("User[id=124,name=darui.wu]", StringMode.IgnoreSpace));
    }

    @Test
    public void testNotAny() {
        String actual = "I am a test.";
        want.string(actual).notAny(the.string().isEqualTo("bcd"), the.string().contains("bce"));
    }

    @Test
    public void testNotAny_Failure() {
        String actual = "I am a test.";
        want.exception(() ->
                        want.string(actual).notAny(the.string().isEqualTo("bcd"), the.string().contains("test")),
                AssertionError.class);
    }

    @Test
    public void testNotAll() {
        String actual = "I am a test.";
        want.string(actual).notAll(the.string().contains("testedObject"), the.string().contains("java"));
    }

    @Test
    public void testNotAll_Failure() {
        String actual = "I am a java test.";
        want.exception(() ->
                        want.string(actual).notAll(the.string().contains("test"), the.string().contains("java")),
                AssertionError.class);
    }
}
