package org.test4j.hamcrest.matcher.calendar;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.test4j.exception.Test4JException;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期类型部分内容(年，月，日，小时，分钟，秒)断言
 *
 * @author darui.wudr
 */
public class DateParterMatcher extends BaseMatcher<Date> {
    private int expected;

    private DateFieldType type;

    public DateParterMatcher(int expected, DateFieldType type) {
        this.expected = expected;
        this.type = type;
    }

    @Override
    public boolean matches(Object actual) {
        if (actual == null) {
            throw new Test4JException("the actual value can't be null");
        }
        Calendar cal = null;

        if (actual instanceof Calendar) {
            cal = (Calendar) actual;
        } else if (actual instanceof Date) {
            cal = Calendar.getInstance();
            cal.setTime((Date) actual);
        } else {
            throw new Test4JException(
                    "the actual value must be a java.util.Date instance or a java.util.Calendar instance");
        }
        int value = cal.get(type.calendarField());
        if (type == DateFieldType.MONTH) {
            return expected == (value + 1);
        } else {
            return expected == value;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format(type.description(), this.expected));
    }

    public enum DateFieldType {
        YEAR {
            @Override
            public int calendarField() {
                return Calendar.YEAR;
            }

            @Override
            public String description() {
                return "the year of expected time must equal to %d";
            }
        },
        MONTH {
            @Override
            public int calendarField() {
                return Calendar.MONTH;
            }

            @Override
            public String description() {
                return "the month of expected time must equal to %d";
            }
        },
        DATE {
            @Override
            public int calendarField() {
                return Calendar.DAY_OF_MONTH;
            }

            @Override
            public String description() {
                return "the day/month of expected time must equal to %d";
            }
        },
        HOUR {
            @Override
            public int calendarField() {
                return Calendar.HOUR_OF_DAY;
            }

            @Override
            public String description() {
                return "the hour of expected time must equal to %d";
            }
        },
        MINUTE {
            @Override
            public int calendarField() {
                return Calendar.MINUTE;
            }

            @Override
            public String description() {
                return "the minute of expected time must equal to %d";
            }
        },
        SECOND {
            @Override
            public int calendarField() {
                return Calendar.SECOND;
            }

            @Override
            public String description() {
                return "the second of expected time must equal to %d";
            }
        };

        public abstract int calendarField();

        public abstract String description();
    }
}
