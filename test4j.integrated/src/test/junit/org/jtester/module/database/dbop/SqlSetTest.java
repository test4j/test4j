package org.jtester.module.database.dbop;

import java.io.File;

import org.jtester.database.table.ITable;
import org.jtester.junit.JTester;
import org.junit.Test;

public class SqlSetTest implements JTester {

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
