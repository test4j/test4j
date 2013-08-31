package org.test4j.hamcrest.matcher.array;

import org.junit.Test;
import org.test4j.hamcrest.matcher.array.SizeOrLengthMatcher.SizeOrLengthMatcherType;
import org.test4j.junit.Test4J;

import ext.test4j.hamcrest.Matcher;
import ext.test4j.hamcrest.MatcherAssert;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SizeOrLengthMatcherTest extends Test4J {
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
