package org.test4j.tools.commons;

import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

import java.util.Calendar;
import java.util.Date;

public class DateUtilTest extends Test4J {

    @Test
    public void testToDateTimeStr() {
        String dateStr = DateHelper.toDateTimeStr(getMockDate(), "yyyy-MM-dd HH:mm:ss");
        want.string(dateStr).isEqualTo("2010-02-12 19:58:55");
    }

    @Test
    public void testToDateTimeStr_MockitExpectation() {
        new MockUp<DateHelper>() {
            @Mock
            public Date now() {
                return getMockDate();
            }
        };
        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2010-02-12 19:58:55");
    }

    @Test
    public void testToDateTimeStr_MockitExpectation_returnSequence() {
        new MockUp<DateHelper>() {
            int index = 0;

            @Mock
            public Date now() {
                return index++ == 0 ? getMockDate() : mockCalendar(2013, 2, 12).getTime();
            }
        };


        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2010-02-12 19:58:55");
        str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2013-02-12 19:58:55");
    }

    @Test
    public void testToDateTimeStr_MockitExpectation2() {
        new MockUp<DateHelper>() {

            @Mock
            public Date now() {
                return mockCalendar(2015, 6, 25).getTime();
            }
        };

        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2015-06-25 19:58:55");
    }

    @Test
    public void testToDateTimeStr_dynamicPartialMock() {
        new MockUp<DateHelper>() {

            @Mock
            public Date now() {
                return mockCalendar(2009, 6, 25).getTime();
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

    public static class MockDateUtil extends MockUp<DateHelper> {
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

    @ParameterizedTest
    @MethodSource("dataParse_Format")
    public void testParse_Format(String input, String output) {
        Date date = DateHelper.parse(input);
        want.date(date).eqByFormat(output);
    }

    public static DataProvider dataParse_Format() {
        return new DataProvider() {
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
    public void testParse_ContainsMillionSecond() {
        Date date = DateHelper.parse("2010-10-20 18:20:36.231");
        want.date(date).eqByFormat("2010/10/20 06:20:36.231", "yyyy/MM/dd hh:mm:ss.SSS");
    }

    @Test
    public void testParse_IllegalFormat() {
        want.exception(() ->
                        DateHelper.parse("2010-10/20 18:20:36.231")
                , RuntimeException.class);
    }
}
