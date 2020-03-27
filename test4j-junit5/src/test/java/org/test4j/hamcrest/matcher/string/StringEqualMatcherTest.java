package org.test4j.hamcrest.matcher.string;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;

public class StringEqualMatcherTest extends Test4J {

    @ParameterizedTest
    @MethodSource("data_StringEqualMatcher_Equals")
    public void testMatches_Equals(String expected, Object actual, boolean doesMatch, StringMode[] modes) {
        StringEqualMatcher matcher = new StringEqualMatcher(expected);

        matcher.setStringModes(modes);
        boolean match = matcher.matches(actual);
        want.bool(match).isEqualTo(doesMatch);
    }

    public static Object[][] data_StringEqualMatcher_Equals() {
        return new Object[][]{
                {null, "", false, null},
                {"a b c", "a\f\rb\tc", true, new StringMode[]{StringMode.IgnoreSpace}},// <br>
                {"ABC", "AbC", true, new StringMode[]{StringMode.IgnoreCase}}, // <br>
                {"'abc\"\n\t\"abc'", "'abc' 'abc'", true,
                        new StringMode[]{StringMode.SameAsQuato, StringMode.SameAsSpace}}, // <br>
                {"a b c", "abc", false, null},// <br>
                {"ABC", "AbC", false, null}, // <br>
                {"'abc\"\n\t\"abc'", "'abc' 'abc'", false, null} // <br>
        };
    }

    @Test
    public void testMatches_ActualIsNull() {
        want.exception(() ->
                        MatcherAssert.assertThat(null, new StringEqualMatcher(""))
                , AssertionError.class);
    }
}
