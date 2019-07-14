package org.test4j.hamcrest.matcher.calendar;

import org.hamcrest.core.IsEqual;

import java.util.Calendar;
import java.util.Date;


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
