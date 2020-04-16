package org.test4j.module.database;


import org.junit.jupiter.api.Test;
import org.test4j.db.ITable;
import org.test4j.db.datamap.table.UserTableMap;
import org.test4j.junit5.Test4J;

import static org.test4j.db.ITable.t_user;

public class DataBaseTest extends Test4J {
    @Test
    public void testValue() {

        db.table(t_user).clean().insert(new UserTableMap(2) {
            {
                this.user_name.values("163", "sohu")
                        .e_mail.generate((index) -> "darui.wu@" + this.get("user_name").row(index) + ".com");
            }
        });
        db.table(t_user).query()
                .sizeEq(2)
                .eqByProperties("e_mail", new String[]{"darui.wu@163.com", "darui.wu@sohu.com"});
    }

    @Test
    public void testDatabase() {
        db.table(ITable.t_user).clean().insert(new UserTableMap(2)
                .id.values("1", "2")
                .first_name.values("darui", "jobs")
                .last_name.values("wu", "he")
        );

        db.query("select id,first_name,last_name from t_user")
                .eqReflect(new UserTableMap(2)
                        .id.values(1, 2)
                        .first_name.values("darui", null)
                        .last_name.values(null, "he")
                );
    }
}
