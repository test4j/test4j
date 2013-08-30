package org.jtester.module.spring.strategy.register;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") }, excludePackages = { "org.jtester.**.UserDao" })
public class SpringBeanRegisterTest_ExcludePackage implements JTester {
    @SpringBeanByName
    private UserService    userService;

    @SpringBeanByName
    private UserAnotherDao userAnotherDao;

    @Test
    public void getSpringBean() {
        want.object(userService).notNull();
        Object o = spring.getBean("userAnotherDao");
        want.object(o).same(userAnotherDao);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void getSpringBean_NoSuchBean() {
        Object userDao = spring.getBean("userDao");
        want.object(userDao).notNull();
    }
}
