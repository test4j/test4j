package org.jtester.module.dbfit.fixture;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class DatabaseFixtureTest_ForPlugin extends JTester {

	@DbFit(when = "DbFixtureTest_ForPlugin.nullValueInsert.wiki")
	public void nullValueInsert() {
	}
}
