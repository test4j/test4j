package org.test4j.module.spring.strategy.register;

import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.test4j.fortest.service.UserAnotherDao;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") }, excludeProperties = { "userDao" })
public class SpringBeanRegisterTest_ExcludeProperty extends Test4J {
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
