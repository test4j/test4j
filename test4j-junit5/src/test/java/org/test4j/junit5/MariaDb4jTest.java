package org.test4j.junit5;

import org.junit.jupiter.api.Test;
import org.test4j.Test4J;
import org.test4j.db.dm.UserDataMap;

public class MariaDb4jTest implements Test4J {
    @Test
    public void test() {
        db.table("t_user").clean()
            .insert(new UserDataMap(true, 2).init()
                .addressId.values(1, 2)
                .age.values(34, 45)
            );
        // db.table("t_user").query().print();
        db.table("t_user").query()
            .eqDataMap(new UserDataMap(true, 2)
                .addressId.values(1, 2)
                .age.values(34, 45)
            );
    }
}