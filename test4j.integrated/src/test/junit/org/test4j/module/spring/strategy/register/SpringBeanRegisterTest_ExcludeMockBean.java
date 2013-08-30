package org.test4j.module.spring.strategy.register;

import mockit.Mocked;
import mockit.NonStrict;

import org.junit.Test;
import org.test4j.fortest.service.UserAnotherDao;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.JTester;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl") })
public class SpringBeanRegisterTest_ExcludeMockBean implements JTester {
    @SpringBeanByName
    private UserService    userService;

    @SpringBeanFrom
    @Mocked
    private UserAnotherDao userAnotherDao;

    @SpringBeanFrom
    @NonStrict
    private UserDao        userDao;

    @Test
    public void getSpringBean_MockedBean() {
        want.object(userService).notNull();
        Object o = spring.getBean("userAnotherDao");
        want.object(o).notNull();
        want.object(o).not(the.object().same(userAnotherDao));
    }

    @Test
    public void getSpringBean_MockBean() {
        Object o = spring.getBean("userDao");
        want.object(o).notNull();
        want.object(o).not(the.object().same(userDao));
    }
}
