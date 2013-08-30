package org.test4j.hamcrest.matcher.string;

import org.junit.Test;
import org.test4j.hamcrest.matcher.string.StringEndWithMatcher;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit.JTester;
import org.test4j.junit.annotations.DataFrom;

import ext.test4j.hamcrest.MatcherAssert;

public class StringEndWithMatcherTest implements JTester {

    @Test
    @DataFrom("endWithDatas")
    public void testMatch(String actual, String expected, boolean doesMatch, StringMode[] modes) {
        StringEndWithMatcher matcher = new StringEndWithMatcher(expected);
        matcher.setStringModes(modes);

        boolean match = matcher.matches(actual);
        want.bool(match).is(doesMatch);
    }

    public static Object[][] endWithDatas() {
        return new Object[][] {
                { "", null, false, null },// <br>
                { "====='abc'", "\"abc\"", true, new StringMode[] { StringMode.IgnoreQuato } },// <br>
                { "=====ABC", "aBc", true, new StringMode[] { StringMode.IgnoreCase } },
                { "=====ABC ", "A\tBC", true, new StringMode[] { StringMode.IgnoreSpace } },
                { "=====abc", "Abc ", false, new StringMode[] { StringMode.IgnoreCase } } };
    }

    @Test(expected = AssertionError.class)
    public void testMatches_ActualIsNull() {
        MatcherAssert.assertThat(null, new StringEndWithMatcher(""));
    }
}
