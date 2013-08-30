package org.test4j.module.dbfit.fixture;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserService;
import org.test4j.module.database.annotations.Transactional;
import org.test4j.module.database.annotations.Transactional.TransactionMode;
import org.test4j.module.dbfit.DbFitTest;
import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringBeanByType;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext({ "org/test4j/fortest/spring/beans.xml", "org/test4j/fortest/spring/data-source.xml" })
@Test(groups = { "test4j", "dbfit" })
public class DatabaseFixtureTest_UserSameDataSource_IBatis extends Test4J {
    @SpringBeanByType
    private UserService userService;

    @Test
    @Transactional(TransactionMode.COMMIT)
    @DbFit(when = "DbFixtureTest_UserSameDataSource_IBatis.getUser.when.wiki", then = "DbFixtureTest_UserSameDataSource_IBatis.getUser.then.wiki")
    public void getUser_VerifyThenWiki_WhenTransactionCommit() {
        User user = DbFitTest.newUser();
        userService.insertUser(user);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @DbFit(when = "DbFixtureTest_UserSameDataSource_IBatis.getUser.when.wiki", then = "DbFixtureTest_UserSameDataSource_IBatis.getUser.then.wiki")
    public void getUser_VerifyThenWiki_WhenTransactionRollBack() {
        User user = DbFitTest.newUser();
        userService.insertUser(user);
    }

    @Test
    @Transactional(TransactionMode.DISABLED)
    @DbFit(when = "DbFixtureTest_UserSameDataSource_IBatis.getUser.when.wiki", then = "DbFixtureTest_UserSameDataSource_IBatis.getUser.then.wiki")
    public void getUser_VerifyThenWiki_WhenTransactionDisabled() {
        User user = DbFitTest.newUser();
        userService.insertUser(user);
    }
}
