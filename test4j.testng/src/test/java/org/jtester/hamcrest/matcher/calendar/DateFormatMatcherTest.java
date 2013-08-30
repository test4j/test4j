package org.jtester.hamcrest.matcher.calendar;

import java.util.Calendar;

import org.jtester.module.JTesterException;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.DateUtilTest;
import org.testng.annotations.Test;

import ext.jtester.hamcrest.MatcherAssert;

@Test(groups = { "jtester", "assertion" })
@SuppressWarnings("unchecked")
public class DateFormatMatcherTest extends JTester {
	static Calendar cal = DateUtilTest.mockCalendar(2010, 1, 3);

	public void test_DateFormat() {
		DateFormatMatcher matcher = new DateFormatMatcher("yyyy-MM-dd", "2010-01-03");
		MatcherAssert.assertThat(cal, matcher);
	}

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

	@Test(expectedExceptions = JTesterException.class)
	public void test_ExpectedNull() {
		new DateFormatMatcher("yyyy-MM-dd", null);
	}

	@Test(expectedExceptions = JTesterException.class)
	public void test_ActualIsNull() {
		DateFormatMatcher matcher = new DateFormatMatcher("yyyy-MM-dd", "2010-01-02");
		MatcherAssert.assertThat(null, matcher);
	}

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
