package org.jtester.testng.database.dbop;

import java.io.File;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.module.database.IDatabase;
import org.jtester.module.database.bean.TddUser;
import org.jtester.module.database.dbop.SqlSet;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@Test(groups = { "jtester", "database" })
public class DBOperatorTest extends JTester implements IDatabase {

    @Test
    public void testClean() {
        db.table(ITable.t_tdd_user).clean().insert(3, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1, 2, 3);
                this.put(IColumn.f_first_name, "2323", "asdf", "adfe");
            }
        });

        db.table(ITable.t_tdd_user).count().isEqualTo(3);
        db.table(ITable.t_tdd_user).clean().insert(1, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_nick_name, "darui.wu");
            }
        });
        db.table(ITable.t_tdd_user).count().isEqualTo(1);

        db.cleanTable(ITable.t_tdd_user).commit();
        db.table(ITable.t_tdd_user).count().isEqualTo(0);
    }

    @Test
    public void testQueryList() {
        db.table(ITable.t_tdd_user).clean().insert(3, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1, 2, 3);
                this.put(IColumn.f_first_name, "2323", "asdf", "adfe");
            }
        });
        db.table(ITable.t_tdd_user).count().isEqualTo(3);
        db.table(ITable.t_tdd_user)
                .query()
                .propertyEq(TddUserTable.IColumn.f_first_name, new String[] { "2323", "asdf", "adfe" },
                        EqMode.IGNORE_ORDER);
    }

    @Test
    public void testCount() {
        db.table(ITable.t_tdd_user).clean().insert(3, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1, 2, 3);
                this.put(IColumn.f_first_name, "2323", "asdf", "adfe");
            }
        });
        db.table(ITable.t_tdd_user).count().isEqualTo(3);
    }

    @Test
    public void testQueryListToList() {
        db.table(ITable.t_tdd_user).clean().insert(3, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1, 2, 3);
                this.put(IColumn.f_first_name, "2323", "asdf", "adfe");
            }
        });
        db.table(ITable.t_tdd_user).queryList(TddUser.class)
                .propertyEq("firstName", new String[] { "2323", "asdf", "adfe" }, EqMode.IGNORE_ORDER);
    }

    @Test
    public void testExecute() {
        db.table(ITable.t_tdd_user).clean().insert(3, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1, 2, 3);
                this.put(IColumn.f_first_name, "2323", "asdf", "adfe");
            }
        });
        db.table(ITable.t_tdd_user).count().isEqualTo(3);
        db.execute("delete from tdd_user").commit();
        db.table(ITable.t_tdd_user).count().isEqualTo(0);
    }

    @Test
    public void testExecute_UseSqlSet() {
        db.table(ITable.t_tdd_user).clean().count().isEqualTo(0);
        db.execute(new SqlSet() {
            {
                sql("insert tdd_user(id, first_name) values(1, \"darui.wu\")");
                sql("insert tdd_user(id, first_name) values(2, \"jobs.he\")");
            }
        }).commit();
        db.table(ITable.t_tdd_user).count().isEqualTo(2);
    }

    public void testExecute_FromFile() {
        final String file = System.getProperty("user.dir")
                + "/src/test/resources/org/jtester/module/database/dbop/sql-demo.sql";
        db.cleanTable(ITable.t_tdd_user).execute(new File(file));

        db.table(ITable.t_tdd_user).count().isEqualTo(2);
    }
}
