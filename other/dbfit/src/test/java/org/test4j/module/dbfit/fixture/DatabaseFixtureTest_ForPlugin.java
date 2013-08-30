package org.test4j.module.dbfit.fixture;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class DatabaseFixtureTest_ForPlugin extends Test4J {

    @DbFit(when = "DbFixtureTest_ForPlugin.nullValueInsert.wiki")
    public void nullValueInsert() {
    }
}
