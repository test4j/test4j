package org.jtester.module.dbfit.fixture.fit;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.DbFit.AUTO;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class InsertFixtureTest extends JTester {

	@DbFit(auto = AUTO.AUTO)
	public void testKeyGenerateFeedback() {

	}

	@DbFit(auto = AUTO.AUTO)
	public void testKeyGenerateFeedback_oracle() {

	}
}
