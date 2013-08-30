package org.jtester.module.jmockit;

import java.util.Calendar;
import java.util.Date;

import mockit.Mock;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.tools.commons.DateHelper;
import org.junit.Test;

/**
 * 验证new MockUp<T> 的作用域
 * 
 * @author darui.wudr
 */
@SpringContext("org/jtester/module/spring/testedbeans/xml/data-source.xml")
@AutoBeanInject
public class MockUpTest_Depends implements JTester {
    @SpringBeanByName(claz = MyImpl.class)
    MyIntf myIntf;

    @Test
    public void testStaticMethod_mock() {
        new MockUp<DateHelper>() {
            @Mock
            public Date now() {
                Calendar cal = mockCalendar(2012, 1, 28);
                return cal.getTime();
            }
        };
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }

    @Test(expected = AssertionError.class)
    // (dependsOnMethods = "testStaticMethod_mock",
    public void testStaticMethod_unmock() {
        String str = DateHelper.currDateTimeStr("MM/dd/yy hh:mm:ss");
        want.string(str).isEqualTo("01/28/12 07:58:55");
    }

    @Test
    public void testMehtod_unmock_beforeMock() {
        String hello = myIntf.hello();
        want.string(hello).isEqualTo("hello");
    }

    @Test
    // (dependsOnMethods = "testMehtod_unmock_beforeMock")
    public void testMethod_mock() {
        new MockUp<MyImpl>() {
            @Mock
            public String hello() {
                return "hello mock!";
            }
        };
        String hello = myIntf.hello();
        want.string(hello).isEqualTo("hello mock!");
    }

    @Test
    // (dependsOnMethods = "testMethod_mock")
    public void testMehtod_unmock_afterMock() {
        String hello = myIntf.hello();
        want.string(hello).isEqualTo("hello");
    }

    public static interface MyIntf {
        String hello();
    }

    public static class MyImpl implements MyIntf {
        public String hello() {
            return "hello";
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
}
