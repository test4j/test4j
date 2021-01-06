package org.test4j.asserts.matcher.string;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

import java.util.Iterator;

public class IgnoreAllSpaceMatcherTest extends Test4J {

    @ParameterizedTest
    @MethodSource("spaceMatcherData")
    public void testMatches(String expected, String actual, boolean doesMatch) {
        StringMatcher matcher = new StringEqualMatcher(expected);
        matcher.setStringModes(StringMode.IgnoreSpace);

        boolean match = matcher.matches(actual);
        want.bool(match).isEqualTo(doesMatch);
    }

    @SuppressWarnings("rawtypes")
    public static Iterator spaceMatcherData() {
        return new DataProvider() {
            {
                data("", "", true);
                data(null, "", false);
                data("\n\t\b\f", "", true);
                data(" d ", "d", true);
            }
        };
    }

    @Test
    public void testMatches_ActualIsNull() {
        want.exception(() ->
                        MatcherAssert.assertThat(null, new StringEqualMatcher(""))
                , AssertionError.class);
    }
}
