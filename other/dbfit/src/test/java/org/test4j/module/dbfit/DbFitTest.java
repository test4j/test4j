package org.test4j.module.dbfit;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserService;
import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j" })
@SpringContext({ "org/test4j/fortest/spring/beans.xml", "classpath:org/test4j/fortest/spring/data-source.xml" })
public class DbFitTest extends Test4J {
	@SpringBeanByName
	private UserService userService;

	@DbFit(when = { "DbFit.paySalary_insert.when.wiki" }, then = "DbFit.paySalary_insert.then.wiki")
	public void paySalary_insert() {
		User user = newUser();
		this.userService.insertUser(user);
	}

	public static User newUser() {
		User user = new User();
		user.setFirst("first name");
		user.setPostcode("320001");
		user.setSarary(23.02d);
		return user;
	}
}
