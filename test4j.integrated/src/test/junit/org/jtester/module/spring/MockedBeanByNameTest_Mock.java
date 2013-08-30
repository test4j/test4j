package org.jtester.module.spring;

import java.util.ArrayList;

import mockit.Mocked;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserService;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.junit.Test;

public class MockedBeanByNameTest_Mock extends MockedBeanByNameTest_Base {
    @SpringBeanByName
    private UserService userService;

    @SpringBeanFrom
    @Mocked
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
}
