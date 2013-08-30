package org.jtester.hamcrest.matcher.string;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

import ext.jtester.hamcrest.MatcherAssert;

public class StringStartWithMatcherTest implements JTester {

    @Test
    @DataFrom("endWithDatas")
    public void testMatch(String actual, String expected, boolean doesMatch, StringMode[] modes) {
        StringStartWithMatcher matcher = new StringStartWithMatcher(expected);
        matcher.setStringModes(modes);

        boolean match = matcher.matches(actual);
        want.bool(match).is(doesMatch);
    }

    public static Object[][] endWithDatas() {
        return new Object[][] {
                { "", null, false, null },// <br>
                { "'abc'=====", "\"abc\"", true, new StringMode[] { StringMode.IgnoreQuato } },// <br>
                { "ABC=====", "aBc", true, new StringMode[] { StringMode.IgnoreCase } },
                { " ABC =====", "A\tBC", true, new StringMode[] { StringMode.IgnoreSpace } },// <br>
                { " abc =====", "Abc", false, new StringMode[] { StringMode.IgnoreCase } } };
    }

    @Test(expected = AssertionError.class)
    public void testMatches_ActualIsNull() {
        MatcherAssert.assertThat(null, new StringStartWithMatcher(""));
    }
}
