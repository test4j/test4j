package org.test4j.hamcrest.matcher.string;

import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.test4j.hamcrest.MatcherAssert;

@Test(groups = { "test4j", "assertion" })
public class StringEqualMatcherTest extends Test4J {

    @Test(dataProvider = "data_StringEqualMatcher_Equals")
    public void testMatches_Equals(String expected, Object actual, boolean doesMatch, StringMode[] modes) {
        StringEqualMatcher matcher = new StringEqualMatcher(expected);

        matcher.setStringModes(modes);
        boolean match = matcher.matches(actual);
        want.bool(match).isEqualTo(doesMatch);
    }

    @DataProvider
    public Object[][] data_StringEqualMatcher_Equals() {
        return new Object[][] {
                { null, "", false, null },
                { "a b c", "a\f\rb\tc", true, new StringMode[] { StringMode.IgnoreSpace } },// <br>
                { "ABC", "AbC", true, new StringMode[] { StringMode.IgnoreCase } }, // <br>
                { "'abc\"\n\t\"abc'", "'abc' 'abc'", true,
                        new StringMode[] { StringMode.SameAsQuato, StringMode.SameAsSpace } }, // <br>
                { "a b c", "abc", false, null },// <br>
                { "ABC", "AbC", false, null }, // <br>
                { "'abc\"\n\t\"abc'", "'abc' 'abc'", false, null } // <br>
        };
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testMatches_ActualIsNull() {
        MatcherAssert.assertThat(null, new StringEqualMatcher(""));
    }
}
