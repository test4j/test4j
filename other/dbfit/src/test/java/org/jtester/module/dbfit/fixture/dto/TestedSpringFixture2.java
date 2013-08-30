package org.jtester.module.dbfit.fixture.dto;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserAnotherDao;
import org.jtester.fortest.service.UserService;
import org.jtester.module.dbfit.fixture.dto.DtoPropertyFixture;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;

@SpringContext({ "org/jtester/fortest/spring//beans.xml", "org/jtester/fortest/spring/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*", impl = "**.*Impl") })
public class TestedSpringFixture2 extends DtoPropertyFixture {
	@SpringBeanByName
	private UserService userService;

	@SpringBeanByName
	private UserAnotherDao anotherUserDao;

	public void insertUser(User user) {

		want.object(anotherUserDao).notNull();
		userService.insertUser(user);
	}
}
