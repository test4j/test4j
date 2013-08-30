package org.test4j.module.dbfit.fixture.dto;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserAnotherDao;
import org.test4j.fortest.service.UserService;
import org.test4j.module.dbfit.fixture.dto.DtoPropertyFixture;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;

@SpringContext({ "org/test4j/fortest/spring//beans.xml", "org/test4j/fortest/spring/data-source.xml" })
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
