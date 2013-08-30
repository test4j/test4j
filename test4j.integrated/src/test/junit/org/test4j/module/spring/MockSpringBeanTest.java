package org.test4j.module.spring;

import java.util.ArrayList;

import mockit.Mocked;

import org.junit.Test;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.Test4J;
import org.test4j.module.database.annotations.Transactional;
import org.test4j.module.database.annotations.Transactional.TransactionMode;
import org.test4j.module.inject.annotations.Inject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/beans.xml",
        "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
public class MockSpringBeanTest implements Test4J {
    @SpringBeanByName
    private UserService userService;

    @Mocked
    @Inject(targets = "userService")
    private UserDao     userDao;

    @Test
    public void paySalary() {
        new Expectations() {
            {
                userDao.findUserByPostcode("310000");
                returns(new ArrayList<User>() {
                    private static final long serialVersionUID = -2799578129563837839L;
                    {
                        this.add(new User(1, 1000d));
                        this.add(new User(2, 1500d));
                        this.add(new User(2, 1800d));
                    }
                });
            }
        };

        double total = this.userService.paySalary("310000");
        want.number(total).isEqualTo(4300d);
    }

    @Test(expected = RuntimeException.class)
    @Transactional(TransactionMode.DISABLED)
    public void paySalary_exception() {
        new Expectations() {
            {
                userDao.findUserByPostcode("310000");
                thenThrow(new RuntimeException());
            }
        };

        this.userService.paySalary("310000");
    }
}
