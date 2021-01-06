package org.test4j.asserts.matcher.calendar;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.Calendar;


@SuppressWarnings("unchecked")
public class DateFormatMatcherTest extends Test4J {
    static Calendar cal = mockCalendar(2010, 1, 3);

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

    @Test
    public void test_ExpectedNull() {
        want.exception(() -> new DateFormatMatcher("yyyy-MM-dd", null), AssertionError.class);
    }

    @Test
    public void test_ActualIsNull() {
        DateFormatMatcher matcher = new DateFormatMatcher("yyyy-MM-dd", "2010-01-02");
        want.exception(() -> MatcherAssert.assertThat(null, matcher), AssertionError.class);
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

    public static Calendar mockCalendar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, 19, 58, 55);
        return cal;
    }
}
