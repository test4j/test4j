package org.test4j.hamcrest.matcher.string;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class StringContainMatcherTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataForStringContains")
    public void testMatches(String actual, String expected, boolean doesMatch, StringMode[] modes) {
        StringContainMatcher matcher = new StringContainMatcher(new String[]{expected}, modes);

        boolean match = matcher.matches(actual);
        want.bool(match).is(doesMatch);
    }

    public static Iterator dataForStringContains() {
        return new DataProvider() {
            {
                data("", null, false, null);
                data("'abc' \"ABCD\"", "'abcd'", true,
                        new StringMode[]{StringMode.IgnoreCase, StringMode.SameAsQuato});
                data(" abc \t\n abcc ", "c abc", true, new StringMode[]{StringMode.SameAsSpace});
                data("'abc' \"ABCD\"", "'abcd'", false, new StringMode[]{StringMode.IgnoreCase});
                data(" abc \t\n abcc ", "c abc", false, new StringMode[]{StringMode.IgnoreCase});
            }
        };
    }

    @Test
    public void testMatches_ActualIsNull() {
        want.exception(() ->
                        MatcherAssert.assertThat(null, new StringContainMatcher(new String[]{""}, null))
                , AssertionError.class);
    }
}
