package org.test4j.module.jmockit;

import mockit.Mock;

import org.junit.Test;
import org.test4j.database.table.ITable;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserService;
import org.test4j.fortest.service.UserServiceImpl;
import org.test4j.junit.Test4J;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/beans.xml",
        "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
public class MockitPartialMockSpringBeanTest extends Test4J {

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

        new MockUserServiceImpl();

        double total = this.userService.paySalary("310000");
        want.number(total).isEqualTo(4000d);

        want.string(wantMock).isEqualTo("unInvoked");
        this.userService.insertUser(new User(1001L));
        want.string(wantMock).isEqualTo("hasInvoked");
    }

    private static String wantMock = "unInvoked";

    public static class MockUserServiceImpl extends MockUp<UserServiceImpl> {
        @Mock
        public void insertUser(User user) {
            MessageHelper.info("user id:" + user.getId());
            want.number(user.getId()).isEqualTo(1001L);
            wantMock = "hasInvoked";
        }
    }
}
