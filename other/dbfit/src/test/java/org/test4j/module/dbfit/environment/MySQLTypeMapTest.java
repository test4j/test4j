package org.test4j.module.dbfit.environment;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class MySQLTypeMapTest extends Test4J {

    @DbFit(when = "mysqltype.when.wiki", then = "mysqltype.then.wiki")
    public void testMySqlType() {

    }
}
