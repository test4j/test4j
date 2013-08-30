package org.test4j.hamcrest.matcher.string;

import java.util.Iterator;

import org.junit.Test;
import org.test4j.hamcrest.matcher.string.StringContainMatcher;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.DataFrom;

import ext.test4j.hamcrest.MatcherAssert;

@SuppressWarnings("rawtypes")
public class StringContainMatcherTest implements Test4J {

    @Test
    @DataFrom("dataForStringContains")
    public void testMatches(String actual, String expected, boolean doesMatch, StringMode[] modes) {
        StringContainMatcher matcher = new StringContainMatcher(new String[] { expected }, modes);

        boolean match = matcher.matches(actual);
        want.bool(match).is(doesMatch);
    }

    public static Iterator dataForStringContains() {
        return new DataIterator() {
            {
                data("", null, false, null);
                data("'abc' \"ABCD\"", "'abcd'", true,
                        new StringMode[] { StringMode.IgnoreCase, StringMode.SameAsQuato });
                data(" abc \t\n abcc ", "c abc", true, new StringMode[] { StringMode.SameAsSpace });
                data("'abc' \"ABCD\"", "'abcd'", false, new StringMode[] { StringMode.IgnoreCase });
                data(" abc \t\n abcc ", "c abc", false, new StringMode[] { StringMode.IgnoreCase });
            }
        };
    }

    @Test(expected = AssertionError.class)
    public void testMatches_ActualIsNull() {
        MatcherAssert.assertThat(null, new StringContainMatcher(new String[] { "" }, null));
    }
}
