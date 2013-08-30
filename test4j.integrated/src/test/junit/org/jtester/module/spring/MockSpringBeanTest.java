package org.jtester.module.spring;

import java.util.ArrayList;

import mockit.Mocked;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.jtester.module.inject.annotations.Inject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class MockSpringBeanTest implements JTester {
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
