package org.jtester.testng.spring.resource;

import mockit.Mock;

import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.testedbeans.resource.UserDaoResourceImpl;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserService;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
@SpringContext({ "org/jtester/module/spring/testedbeans/resource/resource-bean.xml" })
public class ResourceBeanTest_NormalLoading extends JTester {

	@SpringBeanByName
	UserService userService;

	public void testResourceBean() {
		final boolean[] checked = new boolean[] { false };
		new MockUp<UserDaoResourceImpl>() {
			@Mock
			public void insertUser(User user) {
				checked[0] = true;
			}
		};

		userService.insertUser(new User());
		want.bool(checked[0]).is(true);
	}
}
