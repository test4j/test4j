package org.jtester.module.spring.strategy.register;

import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @Scene :使用@AutoBeanInject 来自动注入spring bean，但是无法查找到属性的实现类<br>
 *        抛出异常
 * @author darui.wudr
 */
@Ignore
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl") }, ignoreNotFound = false)
public class SpringBeanRegisterTest_NotFoundImplementClazz_ThrowNotFoundException implements JTester {
    @SpringBeanByName
    UserService userService;

    /**
     * 找不到属性的实现类时_抛出初始化spring异常
     */
    @Test(expected = RuntimeException.class)
    public void throwNotFoundException_AutoBeanInject() {

    }

    /**
     * 测试JTester的BeforeClass异常会导致该类中所有测试方法失败_但不会skip所有的测试
     */
    @Test(expected = RuntimeException.class)
    public void throwNotFoundException() {

    }
}
