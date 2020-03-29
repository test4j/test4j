package org.test4j.tools.commons;

import static org.test4j.tools.commons.DateUtilTest.mockCalendar;

import java.util.Calendar;
import java.util.Date;

import mockit.Mock;
import mockit.MockUp;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.tools.commons.DateUtilTest.MockDateUtil;

@SuppressWarnings({"unused", "rawtypes"})
public class DateUtilTest_jmockit extends Test4J {

    @Test
    public void testCurrDateStr_mockExpectations() {
        new MockUp<DateHelper>() {
            @Mock
            Date now() {
                return mockCalendar().getTime();
            }
        };

        String str = DateHelper.currDateStr();
        want.string(str).isEqualTo("2010-02-12");
    }

    @Test
    public void testCurrDateTimeStr_Delegate() {
        new MockUp<DateHelper>() {
            @Mock
            Date now() {
                return mockCalendar(2011, 1, 27).getTime();
            }
        };
        String str = DateHelper.currDateStr();
        want.string(str).isEqualTo("2011-01-27");
    }

    @Test
    public void testCurrDateStr() {
        new MockDateUtil();
        String str = DateHelper.currDateStr();
        want.string(str).isEqualTo("2010-02-12");
    }

    @Test
    public void testCurrDateTimeStr() {
        new MockDateUtil();
        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2010-02-12 19:58:55");
    }

    @Test
    public void testCurrDateTimeStr_annotations() {
        new MockUp<DateHelper>() {
            @Mock
            Date now() {
                Calendar cal = mockCalendar(2011, 1, 27);
                return cal.getTime();
            }
        };
        String str = DateHelper.currDateTimeStr();
        want.string(str).isEqualTo("2011-01-27 19:58:55");
    }

    @Test
    public void testCurrDateTimeStr_format() {
        new MockDateUtil();
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("02/12/10 07:58:55");
    }

    @Test
    public void testCurrDateTimeStr_Delegate2() {
        new MockUp<DateHelper>() {
            @Mock
            Date now() {
                return mockCalendar(2311, 1, 27).getTime();
            }
        };
        String str = DateHelper.currDateStr();
        want.string(str).isEqualTo("2311-01-27");
    }

    @Test
    public void testCurrDateTimeStr_format2() {
        new MockUp<DateHelper>() {
            @Mock
            public Date now() {
                Calendar cal = DateUtilTest.mockCalendar(2012, 1, 28);
                return cal.getTime();
            }
        };
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }
}
