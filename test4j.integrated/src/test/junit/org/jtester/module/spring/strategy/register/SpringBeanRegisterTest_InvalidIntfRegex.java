package org.jtester.module.spring.strategy.register;

import mockit.Mock;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.tracer.TracerHelper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*222Service") })
public class SpringBeanRegisterTest_InvalidIntfRegex implements JTester {
    @SpringBeanByName
    private UserService    userService;

    @SpringBeanByName
    private UserAnotherDao userAnotherDao;

    @Before
    public void disabledTracer() {
        new MockUp<TracerHelper>() {
            @Mock
            public boolean doesTracerEnabled() {
                return false;
            }
        };
    }

    /**
     * 查找userAnotherDao的实现类失败的case
     */
    @Test
    public void getSpringBean() {
        want.object(userService).notNull();
        Object o = spring.getBean("userAnotherDao");
        want.object(o).same(userAnotherDao);

        Object userDao = spring.getBean("userDao");
        want.object(userDao).notNull();
    }
}
