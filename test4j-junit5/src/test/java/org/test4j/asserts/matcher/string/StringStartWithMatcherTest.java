package org.test4j.asserts.matcher.string;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;

public class StringStartWithMatcherTest extends Test4J {

    @ParameterizedTest
    @MethodSource("endWithDatas")
    public void testMatch(String actual, String expected, boolean doesMatch, StringMode[] modes) {
        StringStartWithMatcher matcher = new StringStartWithMatcher(expected);
        matcher.setStringModes(modes);

        boolean match = matcher.matches(actual);
        want.bool(match).is(doesMatch);
    }

    public static Object[][] endWithDatas() {
        return new Object[][]{
                {"", null, false, null},// <br>
                {"'abc'=====", "\"abc\"", true, new StringMode[]{StringMode.IgnoreQuato}},// <br>
                {"ABC=====", "aBc", true, new StringMode[]{StringMode.IgnoreCase}},
                {" ABC =====", "A\tBC", true, new StringMode[]{StringMode.IgnoreSpace}},// <br>
                {" abc =====", "Abc", false, new StringMode[]{StringMode.IgnoreCase}}};
    }

    @Test
    public void testMatches_ActualIsNull() {
        want.exception(() ->
                        MatcherAssert.assertThat(null, new StringStartWithMatcher(""))
                , AssertionError.class);
    }
}
