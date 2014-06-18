package org.test4j.tools.commons;

import java.util.Calendar;
import java.util.Date;

import mockit.Mock;

import org.test4j.testng.Test4J;
import org.testng.annotations.Test;


@Test(groups = "test4j")
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

    @Test
    public void testCurrDateTimeStr_format() {

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

    @Test(expectedExceptions = AssertionError.class)
    public void testCurrDateTimeStr_format_Exception() {
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }
}
