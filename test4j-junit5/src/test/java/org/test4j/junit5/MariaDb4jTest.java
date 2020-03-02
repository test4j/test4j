package org.test4j.junit5;

import org.junit.jupiter.api.Test;

public class MariaDb4jTest extends Test4J {
    @Test
    public void test(){
        db.table("t_user").clean();
    }
}
