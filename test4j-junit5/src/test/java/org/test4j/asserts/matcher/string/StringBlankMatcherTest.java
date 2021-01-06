package org.test4j.asserts.matcher.string;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class StringBlankMatcherTest extends Test4J {

    @ParameterizedTest
    @MethodSource("blankString_true")
    public void testMatches_True(String input) {
        StringBlankMatcher matcher = new StringBlankMatcher();
        MatcherAssert.assertThat(input, matcher);
    }

    public static Iterator blankString_true() {
        return new DataProvider() {
            {
                data("");
                data("   ");
                data("\n\t\b\f");
            }
        };
    }

    @ParameterizedTest
    @MethodSource("blankString_false")
    public void testMatches_False(String input) {
        StringBlankMatcher matcher = new StringBlankMatcher();
        want.exception(() -> MatcherAssert.assertThat(input, matcher)
                , AssertionError.class
        ).contains("expected is empty string,  but actual", StringMode.IgnoreSpace);
    }

    public static Iterator blankString_false() {
        return new DataProvider()
                .data((String) null)
                .data(" d ");
    }
}
