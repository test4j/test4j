package org.jtester.module.dbfit.fixture;

import org.jtester.fortest.hibernate.User;
import org.jtester.fortest.hibernate.UserService;
import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByType;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "classpath:/org/jtester/fortest/hibernate/project.xml" })
@Test(groups = { "jtester", "dbfit", "hibernate" })
public class DatabaseFixtureTest_UserSameDataSource_Hibernate extends JTester {
	@SpringBeanByType
	private UserService userService;

	@Test
	@Transactional(TransactionMode.ROLLBACK)
	@DbFit(when = "DbFixtureTest_UserSameDataSource.getUser.when.wiki")
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

	@Test
	@Transactional(TransactionMode.COMMIT)
	@DbFit(when = "DbFixtureTest_UserSameDataSource.getUser.when.wiki", then = "DbFixtureTest_UserSameDataSource.getUser.then.wiki")
	public void getUser_VerifyThenWiki_WhenTransactionCommit() {
		User user1 = userService.getUser(1);
		want.object(user1).notNull();

		User user = new User();
		user.setName("new user");
		userService.newUser(user);
	}

	@Test
	@Transactional(TransactionMode.ROLLBACK)
	@DbFit(when = "DbFixtureTest_UserSameDataSource.getUser.when.wiki", then = "DbFixtureTest_UserSameDataSource.getUser.then.wiki")
	public void getUser_VerifyThenWiki_WhenTransactionRollBack() {
		User user1 = userService.getUser(1);
		want.object(user1).notNull();

		User user = new User();
		user.setName("new user");
		userService.newUser(user);
	}

	@Test
	@Transactional(TransactionMode.DISABLED)
	@DbFit(when = "DbFixtureTest_UserSameDataSource.getUser.when.wiki", then = "DbFixtureTest_UserSameDataSource.getUser.then.wiki")
	public void getUser_VerifyThenWiki_WhenTransactionDisabled() {
		User user1 = userService.getUser(1);
		want.object(user1).notNull();

		User user = new User();
		user.setName("new user");
		userService.newUser(user);
	}
}
