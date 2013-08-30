package org.test4j.testng.spring.autowired;

import mockit.Mock;

import org.test4j.fortest.beans.User;
import org.test4j.module.spring.annotations.SpringBeanByType;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.testedbeans.autowired.IUserService;
import org.test4j.module.spring.testedbeans.autowired.UserDaoImpl;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
@SpringContext({ "org/test4j/module/spring/testedbeans/autowired/autowired.xml" })
public class AutoWiredTest_NoSprnigBeanFromMock extends Test4J {

    @SpringBeanByType
    IUserService userService;

    @Test(description = "测试@AutoWired的正常加载方式")
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
