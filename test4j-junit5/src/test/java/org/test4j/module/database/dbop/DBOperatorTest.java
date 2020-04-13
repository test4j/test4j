package org.test4j.module.database.dbop;

import java.io.File;


import org.junit.jupiter.api.Test;
import org.test4j.db.ITable;
import org.test4j.db.datamap.table.UserTableMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.junit5.Test4J;

import static org.test4j.db.mapping.UserMP.*;


@SuppressWarnings("serial")
public class DBOperatorTest extends Test4J {

    @Test
    public void testClean() {
        db.table(ITable.t_user).clean().insert(new UserTableMap(3) {
            {
                this.kv(Column.id, 1, 2, 3);
                this.kv(Column.first_name, "2323", "asdf", "adfe");
            }
        });
        db.table(ITable.t_user).count().isEqualTo(3);

        db.cleanTable(ITable.t_user).commit();
        db.table(ITable.t_user).count().isEqualTo(0);
    }

    @Test
    public void testQueryList() {
        db.table(ITable.t_user).clean().insert(new UserTableMap(3) {
            {
                this.kv(Column.id, 1, 2, 3);
                this.kv(Column.first_name, "2323", "asdf", "adfe");
            }
        });
        db.table(ITable.t_user).count().isEqualTo(3);
        db.table(ITable.t_user)
                .query()
                .eqByProperties(Column.first_name, new String[]{"2323", "asdf", "adfe"},
                        EqMode.IGNORE_ORDER);
    }

    @Test
    public void testCount() {
        db.table(ITable.t_user).clean().insert(new UserTableMap(3) {
            {
                this.kv(Column.id, 1, 2, 3);
                this.kv(Column.first_name, "2323", "asdf", "adfe");
            }
        });
        db.table(ITable.t_user).count().isEqualTo(3);
    }


    @Test
    public void testExecute() {
        db.table(ITable.t_user).clean().insert(new UserTableMap(3) {
            {
                this.kv(Column.id, 1, 2, 3);
                this.kv(Column.first_name, "2323", "asdf", "adfe");
            }
        });
        db.table(ITable.t_user).count().isEqualTo(3);
        db.execute("delete from t_user").commit();
        db.table(ITable.t_user).count().isEqualTo(0);
    }

    @Test
    public void testExecute_UseSqlSet() {
        db.table(ITable.t_user).clean().count().isEqualTo(0);
        db.execute(new SqlSet() {
            {
                sql("insert t_user(id, first_name) values(1, \"darui.wu\")");
                sql("insert t_user(id, first_name) values(2, \"jobs.he\")");
            }
        }).commit();
        db.table(ITable.t_user).count().isEqualTo(2);
    }

    @Test
    public void testExecute_FromFile() {
        final String file = System.getProperty("user.dir")
                + "/src/test/resources/org/test4j/module/database/sql-demo.sql";
        db.cleanTable(ITable.t_user).execute(new File(file));
        db.table(ITable.t_user).count().isEqualTo(2);
    }
}
