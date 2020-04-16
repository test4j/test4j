package org.test4j.hamcrest.iassert.object.impl;

import mockit.Mocked;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.module.inject.Inject;
import org.test4j.tools.commons.DateHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class DateAssertTest extends Test4J {
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Date date = null;
    private static Calendar cal = null;

    static {
        try {
            date = format.parse("2009-04-12 15:36:24");
            cal = Calendar.getInstance();
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private final TestAppClaz testApp = new TestAppClaz();

    @Mocked
    @Inject(targets = "testApp")
    private IDateTest idate;

    @Test
    public void yearIs() {
        want.calendar(cal).isYear(2009).isYear("2009");
    }

    @Test
    public void yearIs_failure1() {
        want.exception(() ->
                        want.calendar(cal).isYear(2009).isYear("20091"),
                AssertionError.class);
    }

    @Test
    public void monthIs() {
        want.calendar(cal).isMonth(4).isMonth("04");
    }

    @Test
    public void dayIs() {
        want.calendar(cal).isDay(12).isDay("12");
    }

    @Test
    public void hourIs() {
        want.calendar(cal).isHour(15).isHour("15");
    }

    @Test
    public void minuteIs() {
        want.calendar(cal).isMinute(36).isMinute("36");
    }

    @Test
    public void secondIs() {
        want.calendar(cal).isSecond(24).isSecond("24");
    }

    private interface IDateTest {
        void setDate(Date date);

        void setCalendar(Calendar cal);
    }

    protected static class TestAppClaz {
        private IDateTest idate;

        public void setTime(Date date, Calendar cal) {
            idate.setDate(date);
            idate.setCalendar(cal);
        }

        public void setIdate(IDateTest idate) {
            this.idate = idate;
        }
    }

    @Test
    public void testFormatEqual() {
        want.calendar(cal).eqByFormat("2009-04-12", "yyyy-MM-dd");
        want.calendar(cal).eqByFormat("2009-04-12");
    }

    @Test
    public void testFormatEqual_AssertMessage() {
        try {
            want.calendar(cal).eqByFormat("2010-01-03", "yyyy-MM-dd");
            want.fail("之前应该已经抛出异常");
        } catch (Throwable e) {
            String error = e.getMessage();
            want.string(error).contains("2009-04-12").contains("yyyy-MM-dd").contains("2010-01-03");
        }
    }

    @Test
    public void testIsEqualTo() {
        Date now = new Date();
        long time = now.getTime();
        want.date(now).isEqualTo(time);
    }

    @Test
    public void testGreaterThen() throws InterruptedException {
        Date history = new Date();
        Thread.sleep(1000);// 延迟一秒判断
        want.date(new Date()).isGreaterThan(history);
        want.date(history).isLessThan(new Date());
    }

    @Test
    public void testPropertyEq() {
        final Date date = DateHelper.parse("2010-06-18 15:26:34");
        Map map = new HashMap() {
            {
                this.put("myDate", date);
            }
        };
        want.object(map).eqByProperties("myDate", "2010-06-18").eqByProperties("myDate", "2010-06-18 15:26:34");
    }
}
