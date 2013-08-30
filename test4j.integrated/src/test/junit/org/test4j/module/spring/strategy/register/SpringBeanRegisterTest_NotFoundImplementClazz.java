package org.test4j.module.spring.strategy.register;

import mockit.NonStrict;

import org.junit.Test;
import org.test4j.fortest.service.UserAnotherDao;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;

/**
 * @Scene :使用@AutoBeanInject 来自动注入spring bean，但是无法查找到属性的实现类<br>
 *        忽略错误，改属性的bean不注入到spring容器
 * @author darui.wudr
 */
@SpringContext({ "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl") })
public class SpringBeanRegisterTest_NotFoundImplementClazz implements Test4J {
    @SpringBeanByName
    UserService    userService;

    @SpringBeanFrom
    @NonStrict
    UserAnotherDao userAnotherDao;

    @Test
    // (description = "找不到属性的实现类时_不注入该springbean")
    public void getSpringBean_AutoBeanInjectNotFindImplement() {
        want.object(userService).notNull();

        UserDao userDao = reflector.getField(userService, "userDao");
        want.object(userDao).isNull();

        UserAnotherDao userAnotherDao = reflector.getField(userService, "userAnotherDao");
        want.object(userAnotherDao).notNull();
    }
}
