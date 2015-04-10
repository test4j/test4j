package org.test4j.module.spring.injected;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanByType;
import org.test4j.module.spring.annotations.SpringContext;

/**
 * 验证SpringBean的注入功能
 * 
 * @author darui.wudr 2013-10-29 下午4:33:55
 */
@SpringContext({ "org/test4j/module/spring/testedbeans/resource/resource-bean.xml" })
public class InjectedByTypeAndByName extends Test4J {
    @Resource(name = "userService")
    UserService userService1;

    @SpringBeanByName("userService")
    UserService userService2;

    @SpringBeanByType
    UserService userService3;

    @Autowired
    UserService userService4;

    @Test
    public void validateSpringBean() {
        want.object(userService1).notNull();
        want.object(userService2).notNull();
        want.object(userService3).notNull();
        want.object(userService4).notNull();
    }
}
