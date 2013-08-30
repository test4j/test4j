package org.test4j.module.spring.resource;

import mockit.Mock;

import org.junit.Test;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.testedbeans.resource.UserDaoResourceImpl;

@SpringContext({ "org/test4j/module/spring/testedbeans/resource/resource-bean.xml" })
public class ResourceBeanTest_NormalLoading implements Test4J {

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
