package org.test4j.hamcrest.matcher.calendar;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.test4j.exception.Test4JException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Calendar或Date的格式化值等于期望值断言器
 *
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public class DateFormatMatcher extends BaseMatcher {

    final String date;
    final SimpleDateFormat format;

    public DateFormatMatcher(String format, String date) {
        if (date == null) {
            throw new Test4JException("the expected value can't be null!");
        }
        this.date = date;
        try {
            this.format = new SimpleDateFormat(format);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("illegal date fomat[" + format + "].", e);
        }
    }

    public DateFormatMatcher(SimpleDateFormat format, String date) {
        if (date == null) {
            throw new Test4JException("the expected value can't be null!");
        }
        this.date = date;
        try {
            this.format = format;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("illegal date fomat[" + format.toPattern() + "].", e);
        }
    }

    String actualDate = null;

    @Override
    public boolean matches(Object actual) {
        if (actual == null) {
            throw new Test4JException("the actual value can't be null");
        }

        if (actual instanceof Calendar) {
            Date cal = ((Calendar) actual).getTime();
            actualDate = format.format(cal);
        } else if (actual instanceof Date) {
            actualDate = format.format((Date) actual);
        } else {
            throw new Test4JException(
                    "the actual value must be a java.util.Date instance or a java.util.Calendar instance");
        }
        boolean isEqual = this.date.equals(actualDate);
        return isEqual;
    }

    @Override
    public void describeTo(Description description) {
        String message = String.format("expected value is %s by format[%s], but actual value is %s.", this.date,
                this.format.toPattern(), this.actualDate);
        description.appendText(message);
    }
}
