package org.jtester.module.jmockit;

import java.util.ArrayList;
import java.util.List;

import mockit.Mock;
import mockit.Mocked;
import mockit.Mockit;

import org.jtester.database.table.ITable;
import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserDaoImpl;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SuppressWarnings("serial")
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class MockitSpringBeanTest implements JTester {
    @SpringBeanByName
    private UserService userService;

    @SpringBeanByName
    private UserDao     userDao;

    @Test
    public void parySalary_mockBean() {
        db.table(ITable.t_tdd_user).clean().insert(2, new DataMap() {
            {
                this.put("id", "1", "2");
                this.put("sarary", "0", "0");
            }
        }).commit();

        new Expectations() {
            @Mocked(methods = "findUserByPostcode")
            UserDaoImpl userDaoImpl;
            {
                when(userDaoImpl.findUserByPostcode(anyString)).thenReturn(getUserList());
            }
        };
        double total = this.userService.paySalary("310000");
        want.number(total).isEqualTo(4300d);

        List<User> users = userDao.findAllUser();
        want.number(users.size()).isEqualTo(2);
    }

    @Test
    public void paySalary() {
        db.table(ITable.t_tdd_user).clean().insert(2, new DataMap() {
            {
                this.put("id", "1", "2");
                this.put("sarary", "0", "0");
            }
        }).commit();
        Mockit.setUpMock(UserDaoImpl.class, MockUserDao.class);

        // mock的行为
        double total = this.userService.paySalary("310000");
        want.number(total).isEqualTo(4300d);

        // not mock的行为
        List<User> users = userDao.findAllUser();
        want.number(users.size()).isEqualTo(2);
        Mockit.tearDownMocks();
    }

    public static class MockUserDao {
        @Mock
        public List<User> findUserByPostcode(String postcode) {
            return getUserList();
        }
    }

    private static List<User> getUserList() {
        return new ArrayList<User>() {
            private static final long serialVersionUID = -2799578129563837839L;
            {
                this.add(new User(1, 1000d));
                this.add(new User(2, 1500d));
                this.add(new User(2, 1800d));
            }
        };
    }
}
