package org.jtester.module.spring.resource;

import mockit.Mock;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.testedbeans.resource.UserDaoResourceImpl;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/resource/resource-bean.xml" })
public class ResourceBeanTest_NormalLoading implements JTester {

    @SpringBeanByName
    UserService userService;

    @Test
    public void testResourceBean() {
        final boolean[] checked = new boolean[] { false };
        new MockUp<UserDaoResourceImpl>() {
            @Mock
            public void insertUser(User user) {
                checked[0] = true;
            }
        };

        userService.insertUser(new User());
        want.bool(checked[0]).is(true);
    }
}
