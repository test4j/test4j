package org.test4j.hamcrest.matcher.string;

import java.util.Iterator;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.DataFrom;

import ext.test4j.hamcrest.MatcherAssert;

public class IgnoreAllSpaceMatcherTest extends Test4J {

    @Test
    @DataFrom("spaceMatcherData")
    public void testMatches(String expected, String actual, boolean doesMatch) {
        StringMatcher matcher = new StringEqualMatcher(expected);
        matcher.setStringModes(StringMode.IgnoreSpace);

        boolean match = matcher.matches(actual);
        want.bool(match).isEqualTo(doesMatch);
    }

    @SuppressWarnings("rawtypes")
    public static Iterator spaceMatcherData() {
        return new DataIterator() {
            {
                data("", "", true);
                data(null, "", false);
                data("\n\t\b\f", "", true);
                data(" d ", "d", true);
            }
        };
    }

    @Test(expected = AssertionError.class)
    public void testMatches_ActualIsNull() {
        MatcherAssert.assertThat(null, new StringEqualMatcher(""));
    }
}
