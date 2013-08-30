package org.jtester.module.dbfit.fixture.dto;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserService;
import org.jtester.module.dbfit.fixture.dto.DtoPropertyFixture;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;

@SpringContext({ "org/jtester/fortest/spring/beans.xml", "org/jtester/fortest/spring/data-source.xml" })
public class TestedSpringFixture extends DtoPropertyFixture {
	@SpringBeanByName
	private UserService userService;

	public void insertUser(User user) {
		userService.insertUser(user);
	}
}
