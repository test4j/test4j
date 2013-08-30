package org.test4j.module.spring.strategy.register;

import org.junit.Test;
import org.test4j.fortest.service.UserAnotherDaoImpl;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.JTester;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.Property;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
public class SpringBeanRegisterTest implements JTester {
    @SpringBeanByName(properties = {
            @Property(name = "userAnotherDao", ref = "userRefDao", clazz = UserAnotherDaoImpl.class),
            @Property(name = "myName", value = "I am test") })
    private UserService userService;

    @Test
    public void testRegister() {
        Object bean = spring.getBean("userRefDao");
        want.object(bean).notNull();

        Object ref = reflector.getField(userService, "userAnotherDao");
        want.object(ref).notNull().same(bean);
    }

    @Test
    public void testRegister2() {
        String value = reflector.getField(userService, "myName");
        want.string(value).isEqualTo("I am test");
    }
}
