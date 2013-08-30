package org.test4j.module.dbfit.fixture.fit;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.annotations.DbFit.AUTO;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class InsertFixtureTest extends Test4J {

    @DbFit(auto = AUTO.AUTO)
    public void testKeyGenerateFeedback() {

    }

    @DbFit(auto = AUTO.AUTO)
    public void testKeyGenerateFeedback_oracle() {

    }
}
