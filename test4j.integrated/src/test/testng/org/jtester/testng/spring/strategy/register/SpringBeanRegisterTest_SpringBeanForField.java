package org.jtester.testng.spring.strategy.register;

import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserDaoImpl2;
import org.jtester.fortest.service.UserService;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
		@BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
@Test(groups = "jtester")
public class SpringBeanRegisterTest_SpringBeanForField extends JTester {
	@SpringBeanByName
	private UserService userService;

	@SpringBeanFrom
	UserDao userDao = new UserDaoImpl2();

	public void getSpringBean() {
		List<User> users = userService.findAllUser();
		// want.collection(users).sizeEq(1).propertyEq("first", "ccc");
		want.collection(users).sizeEq(1).propertyEq("first", new String[] { "ccc" });
	}
}
