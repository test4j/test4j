package org.test4j.module.spring.resource;

import mockit.Mock;
import mockit.Mocked;

import org.junit.Test;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.testedbeans.resource.UserDaoResourceImpl;

@SpringContext({ "org/test4j/module/spring/testedbeans/resource/resource-bean.xml" })
public class ResourceBeanTest_SpringBeanFrom implements Test4J {

    @SpringBeanByName
    UserService userService;

    @SpringBeanFrom
    @Mocked
    UserDao     userDao;

    @Test
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
