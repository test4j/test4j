package org.jtester.module.dbfit.environment;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserService;
import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/fortest/spring/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
		@BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
@Test(groups = "jtester")
public class JTesterTransactionManagerTest extends JTester {
	@SpringBeanByName
	private UserService userService;

	@DbFit(when = "org/jtester/module/dbfit/environment/clean user.wiki", then = "org/jtester/module/dbfit/environment/verify user.wiki")
	public void testNormalTransactonal() {
		userService.insertUser(new User("first", "last"));
	}

	@DbFit(when = "org/jtester/module/dbfit/environment/clean user.wiki", then = "org/jtester/module/dbfit/environment/verify user.wiki")
	@Test
	public void testCommitTransactonal_whenHasBeenRollback() {
		try {
			userService.insertUserException(new User("first", "last"));
		} catch (Throwable e) {
		}
		userService.insertUser(new User("first", "last"));
	}

	@DbFit(when = "org/jtester/module/dbfit/environment/clean user.wiki", then = "org/jtester/module/dbfit/environment/verify user.wiki")
	@Test
	@Transactional(TransactionMode.ROLLBACK)
	public void testRollbackTransactonal_whenHasBeenRollback() {
		try {
			userService.insertUserException(new User("first", "last"));
		} catch (Throwable e) {
		}
		userService.insertUser(new User("first", "last"));
	}
}
