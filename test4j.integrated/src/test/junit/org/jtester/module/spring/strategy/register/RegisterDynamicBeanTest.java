package org.jtester.module.spring.strategy.register;

import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
public class RegisterDynamicBeanTest implements JTester {
    @SpringBeanByName
    protected UserService    userService;

    @SpringBeanByName
    protected UserAnotherDao userAnotherDao;

    @Test
    public void getSpringBean() {
        want.object(userService).notNull();
        Object o = spring.getBean("userAnotherDao");
        want.object(o).same(userAnotherDao);

        Object userDao = spring.getBean("userDao");
        want.object(userDao).notNull();
    }

    /**
     * 测试深度嵌套的setProperty() 时，bean的自动注入
     */
    @Test
    public void callCascadedDao() {
        this.userAnotherDao.callCascadedDao();
    }

    /**
     * 测试自动注入spring bean的时候调用spring init method
     */
    @Test
    public void testSpringInitMethod() {
        int springinit = (Integer) reflector.getField(userAnotherDao, "springinit");
        want.number(springinit).isEqualTo(100);
    }
}
