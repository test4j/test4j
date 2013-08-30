package org.test4j.testng.spring.resource;

import mockit.Mock;
import mockit.Mocked;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.testedbeans.resource.UserDaoResourceImpl;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
@SpringContext({ "org/test4j/module/spring/testedbeans/resource/resource-bean.xml" })
public class ResourceBeanTest_SpringBeanFrom extends Test4J {

    @SpringBeanByName
    UserService userService;

    @SpringBeanFrom
    @Mocked
    UserDao     userDao;

    public void testResourceBean() {
        new MockUp<UserDaoResourceImpl>() {
            @Mock
            public void insertUser(User user) {
                want.fail("this api can't be invoke.");
            }
        };

        new Expectations() {
            {
                userDao.insertUser((User) any);
            }
        };
        userService.insertUser(new User());
    }
}
