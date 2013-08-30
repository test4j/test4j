package org.jtester.hamcrest.matcher.array;

import org.jtester.hamcrest.matcher.array.SizeOrLengthMatcher.SizeOrLengthMatcherType;
import org.jtester.junit.JTester;
import org.junit.Test;

import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.MatcherAssert;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SizeOrLengthMatcherTest implements JTester {
    @Test
    public void test_AssertMessage() {
        Matcher matcher = new SizeOrLengthMatcher(2, SizeOrLengthMatcherType.EQ);
        Object actuals = new int[] { 1, 2, 3 };
        try {
            MatcherAssert.assertThat(actuals, matcher);
            want.fail("之前应该已抛出异常");
        } catch (Throwable e) {
            String message = e.getMessage();
            want.string(message).contains("but actual size is[3].");
        }
    }
}
