package org.test4j.module.dbfit.model;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.annotations.DbFit.AUTO;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
@DbFit(auto = AUTO.AUTO)
public class BigIntegerTest extends Test4J {

    @Test
    public void testInsertBigInteger() {

    }

    @Test(groups = "oracle")
    public void testOracleBinDecimal() {

    }
}
