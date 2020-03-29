package org.test4j.module.database.dbop;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.test4j.db.ITable;
import org.test4j.junit5.Test4J;
import org.test4j.module.database.environment.DBEnvironmentFactory;

public class SqlSetTest extends Test4J {

    @Test
    public void testReadFrom() {
        final String file = System.getProperty("user.dir")
                + "/src/test/resources/org/test4j/module/database/sql-demo.sql";
        db.cleanTable(ITable.t_user).execute(new SqlSet() {
            {
                this.readFrom(DBEnvironmentFactory.getDefaultDBEnvironment(), new File(file));
            }
        });

        db.table(ITable.t_user).count().isEqualTo(2);
    }
}
