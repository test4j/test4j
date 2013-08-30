package org.jtester.hamcrest.iassert.object.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jtester.hamcrest.iassert.common.impl.ComparableAssert;
import org.jtester.hamcrest.iassert.object.intf.IDateAssert;
import org.jtester.hamcrest.matcher.LinkMatcher;
import org.jtester.hamcrest.matcher.calendar.DateFormatMatcher;
import org.jtester.hamcrest.matcher.calendar.DateParterMatcher;
import org.jtester.hamcrest.matcher.calendar.DateParterMatcher.DateFieldType;
import org.jtester.tools.commons.DateHelper;

public class DateAssert<T> extends ComparableAssert<T, IDateAssert<T>> implements IDateAssert<T> {

	public DateAssert(Class<T> clazz) {
		super(DateAssert.class);
		this.value = null;
		this.type = AssertType.MatcherStyle;
		this.link = new LinkMatcher<T>();
		this.assertClaz = IDateAssert.class;
		this.valueClaz = clazz;
	}

	public DateAssert(T value, Class<T> clazz) {
		super(value, DateAssert.class);
		this.type = AssertType.AssertStyle;
		this.value = value;
		this.assertClaz = IDateAssert.class;
		this.valueClaz = clazz;
	}

	public IDateAssert<T> isYear(int year) {
		return this.assertThat(year, DateFieldType.YEAR);
	}

	public IDateAssert<T> isYear(String year) {
		return this.assertThat(year, DateFieldType.YEAR);
	}

	public IDateAssert<T> isDay(int day) {
		return this.assertThat(day, DateFieldType.DATE);
	}

	public IDateAssert<T> isDay(String day) {
		return this.assertThat(day, DateFieldType.DATE);
	}

	public IDateAssert<T> isHour(int hour) {
		return this.assertThat(hour, DateFieldType.HOUR);
	}

	public IDateAssert<T> isHour(String hour) {
		return this.assertThat(hour, DateFieldType.HOUR);
	}

	public IDateAssert<T> isMinute(int minute) {
		return this.assertThat(minute, DateFieldType.MINUTE);
	}

	public IDateAssert<T> isMinute(String minute) {
		return this.assertThat(minute, DateFieldType.MINUTE);
	}

	public IDateAssert<T> isMonth(int month) {
		return this.assertThat(month, DateFieldType.MONTH);
	}

	public IDateAssert<T> isMonth(String month) {
		return this.assertThat(month, DateFieldType.MONTH);
	}

	public IDateAssert<T> isSecond(int second) {
		return this.assertThat(second, DateFieldType.SECOND);
	}

	public IDateAssert<T> isSecond(String second) {
		return this.assertThat(second, DateFieldType.SECOND);
	}

	private IDateAssert<T> assertThat(int value, DateFieldType type) {
		DateParterMatcher matcher = new DateParterMatcher(value, type);
		return this.assertThat(matcher);
	}

	private IDateAssert<T> assertThat(String value, DateFieldType type) {
		DateParterMatcher matcher = new DateParterMatcher(Integer.valueOf(value), type);
		return this.assertThat(matcher);
	}

	public IDateAssert<T> eqByFormat(String expected, String format) {
		DateFormatMatcher matcher = new DateFormatMatcher(format, expected);
		return this.assertThat(matcher);
	}

	public IDateAssert<T> eqByFormat(String expected) {
		SimpleDateFormat df = DateHelper.getDateFormat(expected);
		DateFormatMatcher matcher = new DateFormatMatcher(df, expected);
		return this.assertThat(matcher);
	}

	public IDateAssert<T> isEqualTo(long time) {
		Date date = new Date(time);
		return this.isEqualTo(date);
	}

	public IDateAssert<T> isEqualTo(Calendar calendar) {
		Date date = calendar.getTime();
		return this.isEqualTo(date);
	}
}
