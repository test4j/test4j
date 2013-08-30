package org.test4j.testng.spring.strategy.register;

import org.test4j.fortest.service.UserService;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

/**
 * @Scene :使用@AutoBeanInject 来自动注入spring bean，但是无法查找到属性的实现类<br>
 *        抛出异常
 * @author darui.wudr
 */
@SpringContext({ "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl") }, ignoreNotFound = false)
@Test(groups = { "test4j" })
public class SpringBeanRegisterTest_NotFoundImplementClazz_ThrowNotFoundException extends Test4J {
    @SpringBeanByName
    UserService userService;

    /**
     * 找不到属性的实现类时_抛出初始化spring异常
     */
    @Test(expectedExceptions = RuntimeException.class)
    public void throwNotFoundException_AutoBeanInject() {

    }

    /**
     * 测试test4j的BeforeClass异常会导致该类中所有测试方法失败_但不会skip所有的测试
     */
    @Test(expectedExceptions = RuntimeException.class)
    public void throwNotFoundException() {

    }
}
