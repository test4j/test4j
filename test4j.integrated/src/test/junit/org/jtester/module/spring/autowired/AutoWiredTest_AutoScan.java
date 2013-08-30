package org.jtester.module.spring.autowired;

import mockit.Mock;
import mockit.Mocked;

import org.jtester.fortest.beans.User;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByType;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.testedbeans.autowired.IUserDao;
import org.jtester.module.spring.testedbeans.autowired.IUserService;
import org.jtester.module.spring.testedbeans.autowired.UserDaoImpl;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/autowired/autowired-scan.xml" })
public class AutoWiredTest_AutoScan implements JTester {

    @SpringBeanByType
    IUserService userService;

    @SpringBeanFrom("userDaoImpl")
    @Mocked
    IUserDao     userDao;

    @Test
    // (description = "@AutoWired自动包扫描情况下，使用@SpringBeanFrom来替换spring扫描到的bean")
    public void testAutoWired_AutoScan() {
        new MockUp<UserDaoImpl>() {
            @Mock
            public void insertUser(User user) {
                want.fail("can't be execute");
            }
        };
        new Expectations() {
            {
                userDao.insertUser((User) any);
            }
        };
        userService.insertUser(null);
    }
}
