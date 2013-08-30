package org.jtester.testng.database.environment;

import java.sql.SQLException;
import java.util.List;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserService;
import org.jtester.module.database.IDatabase;
import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.testng.JTester;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

@Test(groups = { "jtester" })
@SuppressWarnings("serial")
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class TransactionManagerTest_DisableMode extends JTester implements IDatabase {
    @SpringBeanByName
    private UserService userService;

    @Transactional(TransactionMode.DISABLED)
    public void testTransactionDisable() throws SQLException {
        db.table(ITable.t_tdd_user).clean().insert(2, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1, 2);
                this.put(IColumn.f_first_name, "darui.wu", "darui.wu2");
            }
        });

        List<User> users1 = this.userService.findAllUser();
        want.collection(users1).sizeEq(2);
        db.table(ITable.t_tdd_user).count().isEqualTo(2);

        try {
            this.userService.insertUserWillException(new User("test"));
            want.fail();
        } catch (Throwable e) {
            // 不做任何事，只是验证Disabled状态下的事务回滚
            want.string(e.getMessage()).contains("insert user exception!");
        }
        List<User> users2 = this.userService.findAllUser();
        want.collection(users2).sizeEq(2);

        db.table(ITable.t_tdd_user).count().isEqualTo(2);
        db.table(ITable.t_tdd_user).insert(1, new TddUserTable() {
            {
                this.put(IColumn.f_id, "7");
                this.put(IColumn.f_first_name, "darui.wu44");
            }
        });
    }

    /**
     * TestNG的dependsOnMethod是非常不靠谱的，为了验证事务<br>
     * 所以这里只写2个方法，用@AfterClass替换dependsOnMethods
     */
    @AfterClass
    public void checkDisabled() {
        db.table(ITable.t_tdd_user).count().isEqualTo(3);
    }
}
