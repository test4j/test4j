package org.jtester.module.spring.strategy.register;

import mockit.Mocked;
import mockit.NonStrict;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

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
