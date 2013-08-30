package org.jtester.module.spring.strategy.register;

import java.util.List;

import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserDaoImpl2;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.JTester;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
public class SpringBeanRegisterTest_SpringBeanForField implements JTester {
    @SpringBeanByName
    private UserService userService;

    @SpringBeanFrom
    UserDao             userDao = new UserDaoImpl2();

    @Test
    public void getSpringBean() {
        List<User> users = userService.findAllUser();
        // want.collection(users).sizeEq(1).propertyEq("first", "ccc");
        want.collection(users).sizeEq(1).propertyEq("first", new String[] { "ccc" });
    }
}
