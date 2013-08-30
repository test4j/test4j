package org.jtester.module.spring;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = @BeanMap(intf = "**.UserAnotherDao", impl = "**.UserAnotherDaoImpl"))
public class JTesterSpringContextTest implements JTester {
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
}
