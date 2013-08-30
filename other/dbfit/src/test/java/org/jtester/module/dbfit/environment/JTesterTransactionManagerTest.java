package org.test4j.module.dbfit.environment;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserService;
import org.test4j.module.database.annotations.Transactional;
import org.test4j.module.database.annotations.Transactional.TransactionMode;
import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.testng.test4j;
import org.testng.annotations.Test;

@SpringContext({ "org/test4j/fortest/spring/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
@Test(groups = "test4j")
public class test4jTransactionManagerTest extends test4j {
    @SpringBeanByName
    private UserService userService;

    @DbFit(when = "org/test4j/module/dbfit/environment/clean user.wiki", then = "org/test4j/module/dbfit/environment/verify user.wiki")
    public void testNormalTransactonal() {
        userService.insertUser(new User("first", "last"));
    }

    @DbFit(when = "org/test4j/module/dbfit/environment/clean user.wiki", then = "org/test4j/module/dbfit/environment/verify user.wiki")
    @Test
    public void testCommitTransactonal_whenHasBeenRollback() {
        try {
            userService.insertUserException(new User("first", "last"));
        } catch (Throwable e) {
        }
        userService.insertUser(new User("first", "last"));
    }

    @DbFit(when = "org/test4j/module/dbfit/environment/clean user.wiki", then = "org/test4j/module/dbfit/environment/verify user.wiki")
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
