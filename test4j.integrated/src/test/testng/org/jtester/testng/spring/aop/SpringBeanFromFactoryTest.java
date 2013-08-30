package org.jtester.testng.spring.aop;

import java.util.Arrays;
import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserService;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.database.IDatabase;
import org.jtester.module.spring.ISpring;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test
@SpringContext({ "org/jtester/module/spring/testedbeans/aop/proxybeans.xml",
		"org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class SpringBeanFromFactoryTest extends JTester implements IDatabase, ISpring {

	UserDao userDao = new UserDao() {
		public List<User> findUserByPostcode(String postcode) {
			return null;
		}

		public void insertUser(User user) {

		}

		public List<User> findAllUser() {
			MessageHelper.info("find all user");
			return Arrays.asList(new User(), new User());
		}

		public int partialNotMock() {
			return 0;
		}

	};

	public void test() {
		UserService userService = spring.getBean("userService");
		List<User> users = userService.findAllUser();
		want.collection(users).sizeEq(2);
	}
}
