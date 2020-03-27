package org.test4j.hamcrest.matcher.array;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.array.SizeOrLengthMatcher.SizeOrLengthMatcherType;
import org.test4j.junit5.Test4J;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SizeOrLengthMatcherTest extends Test4J {
    @Test
    public void test_AssertMessage() {
        Matcher matcher = new SizeOrLengthMatcher(2, SizeOrLengthMatcherType.EQ);
        Object actuals = new int[]{1, 2, 3};
        try {
            MatcherAssert.assertThat(actuals, matcher);
            want.fail("之前应该已抛出异常");
        } catch (Throwable e) {
            String message = e.getMessage();
            want.string(message).contains("but actual size is[3].");
        }
    }
}
