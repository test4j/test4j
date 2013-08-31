package org.test4j.module.database.dbop;

import java.io.File;

import org.junit.Test;
import org.test4j.database.table.ITable;
import org.test4j.junit.Test4J;

public class SqlSetTest extends Test4J {

    @Test
    public void testReadFrom() {
        final String file = System.getProperty("user.dir")
                + "/src/test/resources/org/test4j/module/database/dbop/sql-demo.sql";
        db.cleanTable(ITable.t_tdd_user).execute(new SqlSet() {
            {
                this.readFrom(new File(file));
            }
        });

        db.table(ITable.t_tdd_user).count().isEqualTo(2);
    }
}
