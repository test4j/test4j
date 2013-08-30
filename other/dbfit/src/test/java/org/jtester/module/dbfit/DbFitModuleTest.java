package org.jtester.module.dbfit;

import org.jtester.fortest.hibernate.User;
import org.jtester.fortest.hibernate.UserService;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.FitVar;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByType;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "JTester", "hibernate" })
@SpringContext({ "classpath:/org/jtester/fortest/hibernate/project.xml" })
public class DbFitModuleTest extends JTester {
	@SpringBeanByType
	private UserService userService;

	@DbFit(when = "org/jtester/module/core/DbFitModuleTest.getUser.wiki")
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
