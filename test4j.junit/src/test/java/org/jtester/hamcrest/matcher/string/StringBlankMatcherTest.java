package org.jtester.hamcrest.matcher.string;

import java.util.Iterator;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

import ext.jtester.hamcrest.MatcherAssert;

@SuppressWarnings("rawtypes")
public class StringBlankMatcherTest implements JTester {

    @Test
    @DataFrom("blankString_true")
    public void testMatches_True(String input) {
        StringBlankMatcher matcher = new StringBlankMatcher();
        MatcherAssert.assertThat(input, matcher);
    }

    public static Iterator blankString_true() {
        return new DataIterator() {
            {
                data("");
                data("   ");
                data("\n\t\b\f");
            }
        };
    }

    @Test
    @DataFrom("blankString_false")
    public void testMatches_False(String input) {
        StringBlankMatcher matcher = new StringBlankMatcher();
        try {
            MatcherAssert.assertThat(input, matcher);
        } catch (Error e) {
            String message = e.getMessage();
            want.string(message).contains("expected is empty string,  but actual", StringMode.IgnoreSpace);
        }
    }

    public static Iterator blankString_false() {
        return new DataIterator() {
            {
                data((String) null);
                data(" d ");
            }
        };
    }
}
