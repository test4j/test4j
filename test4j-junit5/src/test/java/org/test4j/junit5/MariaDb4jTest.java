package org.test4j.junit5;

import org.junit.jupiter.api.Test;
import org.test4j.db.datamap.table.UserTableMap;

public class MariaDb4jTest extends Test4J {
    @Test
    public void test() {
        db.table("t_user").clean()
                .insert(new UserTableMap(2).init()
                        .address_id.values(1, 2)
                        .age.values(34, 45)
                );
        // db.table("t_user").query().print();
        db.table("t_user").query()
                .eqDataMap(new UserTableMap(2)
                        .address_id.values(1, 2)
                        .age.values(34, 45)
                );
    }
}
