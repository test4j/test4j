package org.test4j.testng.spring.strategy.register;

import mockit.Mock;

import org.test4j.fortest.service.UserAnotherDao;
import org.test4j.fortest.service.UserService;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.tracer.TracerHelper;
import org.test4j.testng.Test4J;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*222Service") })
public class SpringBeanRegisterTest_InvalidIntfRegex extends Test4J implements IDatabase, ISpring {
    @SpringBeanByName
    private UserService    userService;

    @SpringBeanByName
    private UserAnotherDao userAnotherDao;

    @BeforeMethod
    public void disabledTracer() {
        new MockUp<TracerHelper>() {
            @Mock
            public boolean doesTracerEnabled() {
                return false;
            }
        };
    }

    @Test(description = "查找userAnotherDao的实现类失败的case", groups = "for-test")
    public void getSpringBean() {
        want.object(userService).notNull();
        Object o = spring.getBean("userAnotherDao");
        want.object(o).same(userAnotherDao);

        Object userDao = spring.getBean("userDao");
        want.object(userDao).notNull();
    }
}
