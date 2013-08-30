package org.jtester.module.jmockit;

import mockit.Mock;
import mockit.Mockit;

import org.jtester.database.table.ITable;
import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserService;
import org.jtester.fortest.service.UserServiceImpl;
import org.jtester.junit.JTester;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class MockitPartialMockSpringBeanTest implements JTester {

    @SpringBeanByName
    private UserService userService;

    @Test
    @SuppressWarnings("serial")
    public void paySalary() {
        db.table(ITable.t_tdd_user).clean().insert(5, new DataMap() {
            {
                this.put("id", "1", "2", "3", "4", "5");
                this.put("post_code", "310000", "310000", "352200", "310000", "DddDdd");
                this.put("sarary", "1000", "1200", "1500", "1800", "2300");
            }
        }).commit();

        Mockit.setUpMock(UserServiceImpl.class, MockUserServiceImpl.class);

        double total = this.userService.paySalary("310000");
        want.number(total).isEqualTo(4000d);

        want.string(wantMock).isEqualTo("unInvoked");
        this.userService.insertUser(new User(1001L));
        want.string(wantMock).isEqualTo("hasInvoked");
    }

    private static String wantMock = "unInvoked";

    public static class MockUserServiceImpl {
        @Mock
        public void insertUser(User user) {
            MessageHelper.info("user id:" + user.getId());
            want.number(user.getId()).isEqualTo(1001L);
            wantMock = "hasInvoked";
        }
    }
}
