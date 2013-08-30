package org.test4j.hamcrest.matcher.string;

import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ext.test4j.hamcrest.MatcherAssert;

@Test(groups = { "test4j", "assertion" })
public class StringBlankMatcherTest extends Test4J {

    @Test(dataProvider = "blankString_true")
    public void testMatches_True(String input) {
        StringBlankMatcher matcher = new StringBlankMatcher();
        MatcherAssert.assertThat(input, matcher);
    }

    @DataProvider
    public Object[][] blankString_true() {
        return new Object[][] { { "" },// <br>
                { "   " }, /** <br> */
                { "\n\t\b\f" } /** <br> */
        };
    }

    @Test(dataProvider = "blankString_false")
    public void testMatches_False(String input) {
        StringBlankMatcher matcher = new StringBlankMatcher();
        try {
            MatcherAssert.assertThat(input, matcher);
        } catch (Error e) {
            String message = e.getMessage();
            want.string(message).contains("expected is empty string,  but actual", StringMode.IgnoreSpace);
        }
    }

    @DataProvider
    public Object[][] blankString_false() {
        return new Object[][] { { null }, // <br>
                { " d " } /** <br> */
        };
    }
}
