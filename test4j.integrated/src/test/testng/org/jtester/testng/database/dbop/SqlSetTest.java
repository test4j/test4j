package org.jtester.testng.database.dbop;

import java.io.File;

import org.jtester.database.table.ITable;
import org.jtester.module.database.IDatabase;
import org.jtester.module.database.dbop.SqlSet;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "database" })
public class SqlSetTest extends JTester implements IDatabase {

    @Test
    public void testReadFrom() {
        final String file = System.getProperty("user.dir")
                + "/src/test/resources/org/jtester/module/database/dbop/sql-demo.sql";
        db.cleanTable(ITable.t_tdd_user).execute(new SqlSet() {
            {
                this.readFrom(new File(file));
            }
        });

        db.table(ITable.t_tdd_user).count().isEqualTo(2);
    }
}
