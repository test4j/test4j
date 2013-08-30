package org.test4j.testng.spring.strategy.register;

import mockit.Mocked;
import mockit.NonStrict;

import org.test4j.fortest.service.UserAnotherDao;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl") })
@Test(groups = "test4j")
public class SpringBeanRegisterTest_ExcludeMockBean extends Test4J implements IDatabase, ISpring {
    @SpringBeanByName
    private UserService    userService;

    @SpringBeanFrom
    @Mocked
    private UserAnotherDao userAnotherDao;

    @SpringBeanFrom
    @NonStrict
    private UserDao        userDao;

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
