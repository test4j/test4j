package org.test4j.hamcrest.matcher.string;

import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.test4j.hamcrest.MatcherAssert;

@Test(groups = { "test4j", "assertion" })
public class IgnoreAllSpaceMatcherTest extends Test4J {

    @Test(dataProvider = "spaceMatcherData")
    public void testMatches(String expected, String actual, boolean doesMatch) {
        StringMatcher matcher = new StringEqualMatcher(expected);
        matcher.setStringModes(StringMode.IgnoreSpace);

        boolean match = matcher.matches(actual);
        want.bool(match).isEqualTo(doesMatch);
    }

    @DataProvider
    public Object[][] spaceMatcherData() {
        return new Object[][] { { "", "", true },// <br>
                { null, "", false }, /** <br> */
                { "\n\t\b\f", "", true }, /** <br> */
                { " d ", "d", true } /** <br> */
        };
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testMatches_ActualIsNull() {
        MatcherAssert.assertThat(null, new StringEqualMatcher(""));
    }
}
