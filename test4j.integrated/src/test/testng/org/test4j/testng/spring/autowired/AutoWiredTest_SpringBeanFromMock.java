package org.test4j.testng.spring.autowired;

import mockit.Mock;
import mockit.Mocked;

import org.test4j.fortest.beans.User;
import org.test4j.module.spring.annotations.SpringBeanByType;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.testedbeans.autowired.IUserDao;
import org.test4j.module.spring.testedbeans.autowired.IUserService;
import org.test4j.module.spring.testedbeans.autowired.UserDaoImpl;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
@SpringContext({ "org/test4j/module/spring/testedbeans/autowired/autowired.xml" })
public class AutoWiredTest_SpringBeanFromMock extends Test4J {

    @SpringBeanByType
    IUserService userService;

    @SpringBeanFrom
    @Mocked
    IUserDao     userDao;

    @Test(description = "测试@AutoWired加载的bean被@SpringBeanFrom方式注入的bean替换")
    public void testAutoWired() {
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
