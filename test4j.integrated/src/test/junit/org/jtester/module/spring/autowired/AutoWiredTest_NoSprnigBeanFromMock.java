package org.jtester.module.spring.autowired;

import mockit.Mock;

import org.jtester.module.spring.annotations.SpringBeanByType;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.testedbeans.autowired.IUserService;
import org.jtester.module.spring.testedbeans.autowired.UserDaoImpl;
import org.junit.Test;
import org.test4j.fortest.beans.User;
import org.test4j.junit.JTester;

@SpringContext({ "org/jtester/module/spring/testedbeans/autowired/autowired.xml" })
public class AutoWiredTest_NoSprnigBeanFromMock implements JTester {

    @SpringBeanByType
    IUserService userService;

    @Test
    // (description = "测试@AutoWired的正常加载方式")
    public void testAutoWired() {
        final boolean[] checked = new boolean[] { false };
        new MockUp<UserDaoImpl>() {
            @Mock
            public void insertUser(User user) {
                checked[0] = true;
            }
        };

        userService.insertUser(new User("daui.wu"));
        want.bool(checked[0]).is(true);
    }
}
