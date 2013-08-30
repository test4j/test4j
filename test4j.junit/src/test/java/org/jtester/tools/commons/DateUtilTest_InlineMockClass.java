package org.jtester.tools.commons;

import java.util.Calendar;
import java.util.Date;

import mockit.Mock;
import mockit.Mockit;

import org.jtester.junit.JTester;
import org.junit.Test;

public class DateUtilTest_InlineMockClass implements JTester {
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

    @Test
    public void testCurrDateTimeStr_format() {
        Mockit.setUpMock(DateHelper.class, new Object() {
            @Mock
            public Date now() {
                Calendar cal = DateUtilTest.mockCalendar(2012, 1, 28);
                return cal.getTime();
            }
        });
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }

    @Test(expected = AssertionError.class)
    public void testCurrDateTimeStr_format_Exception() {
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }
}
