package org.jtester.module.spring.strategy.register;

import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

/**
 * 验证多个表达符合接口类型时获取实现
 * 
 * @author darui.wudr
 */
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.impl.*DaoImpl"), /** <br> */
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
public class SpringBeanRegisterTest_MultiPattern implements JTester {
    @SpringBeanByName
    private UserService userService;

    @Test
    public void checkUserAnotherDao_NotFound() {
        Object userDao = spring.getBean("userAnotherDao");
        want.object(userDao).notNull();
    }

    @Test
    public void checkUserServiceBean() {
        want.object(userService).notNull();
        Object o = spring.getBean("userService");
        want.object(o).same(userService);
    }
}
