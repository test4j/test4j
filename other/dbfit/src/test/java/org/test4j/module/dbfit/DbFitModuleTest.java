package org.test4j.module.dbfit;

import org.test4j.fortest.hibernate.User;
import org.test4j.fortest.hibernate.UserService;
import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.annotations.FitVar;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringBeanByType;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "hibernate" })
@SpringContext({ "classpath:/org/test4j/fortest/hibernate/project.xml" })
public class DbFitModuleTest extends Test4J {
	@SpringBeanByType
	private UserService userService;

	@DbFit(when = "org/test4j/module/core/DbFitModuleTest.getUser.wiki")
	public void getUser() {
		User user1 = userService.getUser(1);
		want.object(user1).notNull();
		User user2 = userService.getUser(2);
		want.object(user2).notNull();

		User user3 = userService.getUser(3);
		want.object(user3).isNull();
		User user4 = userService.getUser(4);
		want.object(user4).isNull();
	}

	@DbFit(when = "DbFitModuleTest.testCn.utf8.when.wiki", then = "DbFitModuleTest.testCn.utf8.then.wiki")
	public void testCn_utf8_utf8() {
	}

	@DbFit(when = "DbFitModuleTest.testCn.utf8.when.wiki", then = "DbFitModuleTest.testCn.gbk.then.wiki")
	public void testCn_utf8_gbk() {
	}

	@DbFit(when = "DbFitModuleTest.testCn.gbk.when.wiki", then = "DbFitModuleTest.testCn.utf8.then.wiki")
	public void testCn_gbk_utf8() {
	}

	@DbFit(when = "DbFitModuleTest.testCn.gbk.when.wiki", then = "DbFitModuleTest.testCn.gbk.then.wiki")
	public void testCn_gbk_gbk() {
	}

	@DbFit(when = "DbFitModuleTest.exactFitVar.when.wiki", then = "DbFitModuleTest.exactFitVar.then.wiki", vars = {
			@FitVar(key = "wikiName", value = "darui.wu"), @FitVar(key = "myid", value = "2") })
	public void exactFitVar() {

	}
}
