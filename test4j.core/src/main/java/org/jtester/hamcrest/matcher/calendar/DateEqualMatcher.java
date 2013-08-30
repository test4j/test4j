package org.jtester.hamcrest.matcher.calendar;

import java.util.Calendar;
import java.util.Date;

import ext.jtester.hamcrest.core.IsEqual;

public class DateEqualMatcher extends IsEqual<Date> {

	public DateEqualMatcher(Date date) {
		super(date);
	}

	public static DateEqualMatcher newMatcher(long time) {
		Date date = new Date(time);
		return new DateEqualMatcher(date);
	}

	public static DateEqualMatcher newMatcher(Date date) {
		return new DateEqualMatcher(date);
	}

	public static DateEqualMatcher newMatcher(Calendar cal) {
		Date date = cal.getTime();
		return new DateEqualMatcher(date);
	}
}
