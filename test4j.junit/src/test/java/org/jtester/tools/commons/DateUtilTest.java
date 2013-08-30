package org.jtester.tools.commons;

import java.util.Calendar;
import java.util.Date;

import mockit.Mock;
import mockit.Mocked;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class DateUtilTest implements JTester {

    @Test
    public void testToDateTimeStr() {
        String dateStr = DateHelper.toDateTimeStr(getMockDate(), "yyyy-MM-dd HH:mm:ss");
        want.string(dateStr).isEqualTo("2010-02-12 19:58:55");
    }

    @Test
    public void testToDateTimeStr_MockitExpectation() {
        new NonStrictExpectations() {
            @Mocked(methods = "now")
            DateHelper dateUtil;
            {
                DateHelper.now();
                returns(getMockDate());
            }
        };
        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2010-02-12 19:58:55");
    }

    @Test
    public void testToDateTimeStr_MockitExpectation_returnSequence() {
        new NonStrictExpectations() {
            @Mocked(methods = "now")
            DateHelper dateUtil;
            {
                DateHelper.now();
                returns(getMockDate(), mockCalendar(2013, 2, 12).getTime());
            }
        };
        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2010-02-12 19:58:55");
        str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2013-02-12 19:58:55");
    }

    @Test
    public void testToDateTimeStr_MockitExpectation2() {
        new NonStrictExpectations() {
            @Mocked(methods = "now")
            DateHelper dateUtil;
            {
                DateHelper.now();
                returns(mockCalendar(2015, 6, 25).getTime());
            }
        };
        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2015-06-25 19:58:55");
    }

    @Test
    public void testToDateTimeStr_dynamicPartialMock() {
        new Expectations(DateHelper.class) {
            {
                DateHelper.now();
                returns(mockCalendar(2009, 6, 25).getTime());
            }
        };
        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2009-06-25 19:58:55");
    }

    public static final Date getMockDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2010, 1, 12, 19, 58, 55);
        return cal.getTime();
    }

    public static class MockDateUtil {
        @Mock
        public static final Date now() {
            Calendar cal = mockCalendar();
            return cal.getTime();
        }
    }

    public static Calendar mockCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(2010, 1, 12, 19, 58, 55);
        return cal;
    }

    public static Calendar mockCalendar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, 19, 58, 55);
        return cal;
    }

    @Test
    @DataFrom("dataParse_Format")
    public void testParse_Format(String input, String output) {
        Date date = DateHelper.parse(input);
        want.date(date).eqByFormat(output);
    }

    public static DataIterator dataParse_Format() {
        return new DataIterator() {
            {
                data("2011-09-12 12:23:34", "2011-09-12 12:23:34");
                data("2011-09-13  12:23:34", "2011-09-13 12:23:34");
                data("2011-09-14", "2011-09-14");
                data("2011-9-14", "2011-09-14");
                data("2011-09-15 12:23:34.1", "2011-09-15 12:23:34");
                data("2011-09-16 12:23:34.145", "2011-09-16 12:23:34");
                data(" 2011-09-17  \t12:23:34 ", "2011-09-17 12:23:34");
            }
        };
    }

    @Test
    public void testParse() {
        Date date = DateHelper.parse("2010-10-20");
        want.date(date).eqByFormat("2010/10/20", "yyyy/MM/dd");
    }

    @Test
    public void testParse_ContainsMillioSecond() {
        Date date = DateHelper.parse("2010-10-20 18:20:36.231");
        want.date(date).eqByFormat("2010/10/20 06:20:36.231", "yyyy/MM/dd hh:mm:ss.SSS");
    }

    @Test(expected = RuntimeException.class)
    public void testParse_IllegalFormat() {
        DateHelper.parse("2010-10/20 18:20:36.231");
    }
}
