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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = { "jtester" })
@SuppressWarnings("serial")
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class TransactionManagerTest_RollbackMode extends JTester implements IDatabase {
    @SpringBeanByName
    private UserService userService;

    @BeforeClass
    public static void cleanTable() {
        db.table(ITable.t_tdd_user).clean().insert(new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_first_name, "jobs1");
            }
        }).commit();
    }

    @Transactional(TransactionMode.ROLLBACK)
    public void testTransactionRollback() throws SQLException {
        db.table(ITable.t_tdd_user).clean().insert(2, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1, 2);
                this.put(IColumn.f_first_name, "darui.wu", "darui.wu2");
            }
        });

        List<User> users1 = this.userService.findAllUser();
        want.collection(users1).sizeEq(2);
        db.table(ITable.t_tdd_user).count().isEqualTo(2);

        this.userService.insertUser(new User("test"));

        List<User> users2 = this.userService.findAllUser();
        want.collection(users2).sizeEq(3);
        db.table(ITable.t_tdd_user).count().isEqualTo(3);
    }

    @AfterClass
    public void checkTransactionRollback() {
        db.table(ITable.t_tdd_user).count().isEqualTo(1);
    }
}
