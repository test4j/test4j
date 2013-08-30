package org.jtester.tools.commons;

import java.util.Calendar;
import java.util.Date;

import javax.security.auth.login.LoginContext;

import mockit.Mock;
import mockit.MockClass;
import mockit.UsingMocksAndStubs;

import org.jtester.junit.JTester;
import org.jtester.tools.commons.DateUtilTest_MockClass.MockDateUtil;
import org.junit.Test;

@UsingMocksAndStubs({ MockDateUtil.class })
public class DateUtilTest_MockClass implements JTester {
    @MockClass(realClass = DateHelper.class)
    public static class MockDateUtil {
        @Mock
        public static Date now() {
            Calendar cal = DateUtilTest.mockCalendar(2012, 1, 28);
            return cal.getTime();
        }
    }

    @Test
    public void testCurrDateTimeStr_format() {
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }

    @MockClass(realClass = LoginContext.class, stubs = { "(String)", "logout" })
    final class MockLoginContextWithStubs {
        @Mock
        void login() {
        } // this could also have been an stub
    }
}
