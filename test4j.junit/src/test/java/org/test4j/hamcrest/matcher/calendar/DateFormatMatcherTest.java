package org.test4j.hamcrest.matcher.calendar;

import java.util.Calendar;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.module.Test4JException;
import org.test4j.tools.commons.DateUtilTest;

import ext.test4j.hamcrest.MatcherAssert;

@SuppressWarnings("unchecked")
public class DateFormatMatcherTest extends Test4J {
    static Calendar cal = DateUtilTest.mockCalendar(2010, 1, 3);

    @Test
    public void test_DateFormat() {
        DateFormatMatcher matcher = new DateFormatMatcher("yyyy-MM-dd", "2010-01-03");
        MatcherAssert.assertThat(cal, matcher);
    }

    @Test
    public void test_DateFormat_Message() {
        DateFormatMatcher matcher = new DateFormatMatcher("yyyy-MM-dd", "2010-01-02");
        try {
            MatcherAssert.assertThat(cal, matcher);
            want.fail("之前应该已经抛出异常");
        } catch (Throwable e) {
            String error = e.getMessage();
            want.string(error).contains("2010-01-02").contains("yyyy-MM-dd").contains("2010-01-03");
        }
    }

    @Test(expected = Test4JException.class)
    public void test_ExpectedNull() {
        new DateFormatMatcher("yyyy-MM-dd", null);
    }

    @Test(expected = Test4JException.class)
    public void test_ActualIsNull() {
        DateFormatMatcher matcher = new DateFormatMatcher("yyyy-MM-dd", "2010-01-02");
        MatcherAssert.assertThat(null, matcher);
    }

    @Test
    public void test_fomat_Exception() {
        try {
            new DateFormatMatcher("yyyy-xx-dd", "2010-01-02");
            want.fail("之前应该已经抛出异常");
        } catch (Throwable e) {
            String message = e.getMessage();
            want.string(message).isEqualTo("illegal date fomat[yyyy-xx-dd].");
        }
    }
}
