package org.jtester.module.spring.strategy.register.types;

import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.testedbeans.resource.UserServiceResourceImpl;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml",
        "org/jtester/module/spring/testedbeans/xml/annotation-config.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*", impl = "**.*Impl") })
public class ResourcePropertiesRegisterTest implements JTester {

    @SpringBeanByName(claz = UserServiceResourceImpl.class)
    UserService userService;

    @Test
    // (description = "测试@Resource属性的自动注入")
    public void testRegisterProperties() {
        want.object(userService).notNull();
        Object bean1 = spring.getBean("userDao");
        want.object(bean1).notNull();
        Object bean2 = reflector.getField(userService, "userDao");
        want.object(bean2).notNull().same(bean1);
    }
}
