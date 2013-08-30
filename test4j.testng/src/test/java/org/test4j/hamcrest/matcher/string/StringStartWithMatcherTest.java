package org.test4j.hamcrest.matcher.string;

import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.test4j.hamcrest.MatcherAssert;

@Test(groups = { "test4j", "assertion" })
public class StringStartWithMatcherTest extends Test4J {

    @Test(dataProvider = "endWithDatas")
    public void testMatch(String actual, String expected, boolean doesMatch, StringMode[] modes) {
        StringStartWithMatcher matcher = new StringStartWithMatcher(expected);
        matcher.setStringModes(modes);

        boolean match = matcher.matches(actual);
        want.bool(match).is(doesMatch);
    }

    @DataProvider
    public Object[][] endWithDatas() {
        return new Object[][] {
                { "", null, false, null },// <br>
                { "'abc'=====", "\"abc\"", true, new StringMode[] { StringMode.IgnoreQuato } },// <br>
                { "ABC=====", "aBc", true, new StringMode[] { StringMode.IgnoreCase } },
                { " ABC =====", "A\tBC", true, new StringMode[] { StringMode.IgnoreSpace } },// <br>
                { " abc =====", "Abc", false, new StringMode[] { StringMode.IgnoreCase } } };
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testMatches_ActualIsNull() {
        MatcherAssert.assertThat(null, new StringStartWithMatcher(""));
    }
}
