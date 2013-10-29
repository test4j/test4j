package org.test4j.tools.commons;

import java.util.Calendar;
import java.util.Date;

import mockit.Mock;

import org.junit.Test;
import org.test4j.junit.Test4J;

public class DateUtilTest_InlineMockClass extends Test4J {
    @Test
    public void testCurrDateTimeStr_MockUp() throws Exception {
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

    @SuppressWarnings("rawtypes")
    @Test
    public void testCurrDateTimeStr_format() {
        new MockUp(DateHelper.class) {
            @Mock
            public Date now() {
                Calendar cal = DateUtilTest.mockCalendar(2012, 1, 28);
                return cal.getTime();
            }
        };
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }

    @Test(expected = AssertionError.class)
    public void testCurrDateTimeStr_format_Exception() {
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }
}
