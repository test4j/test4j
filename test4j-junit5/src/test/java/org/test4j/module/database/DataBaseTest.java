package org.test4j.module.database;


import org.junit.jupiter.api.Test;
import org.test4j.Test4J;
import org.test4j.db.ITable;
import org.test4j.db.dm.UserDataMap;


import static org.test4j.db.ITable.t_user;

public class DataBaseTest implements Test4J {
    @Test
    public void testValue() {

        db.table(t_user).clean().insert(new UserDataMap(true, 2) {
            {
                this.userName.values("163", "sohu")
                    .eMail.generate((index) -> "darui.wu@" + this.get("user_name").row(index) + ".com");
            }
        });
        db.table(t_user).query()
            .sizeEq(2)
            .eqByProperties("e_mail", new String[]{"darui.wu@163.com", "darui.wu@sohu.com"});
    }

    @Test
    public void testDatabase() {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true, 2)
            .id.values("1", "2")
            .firstName.values("darui", "jobs")
            .lastName.values("wu", "he")
        );

        db.query("select id,first_name,last_name from t_user")
            .eqReflect(new UserDataMap(true, 2)
                .id.values(1, 2)
                .firstName.values("darui", (Object) null)
                .lastName.values(null, "he")
            );
    }
}