package org.jtester.module.dbfit;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.FitVar;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester" })
public class DbFitModuleTest_WithSQL extends JTester {

	@DbFit(when = "org/jtester/module/dbfit/data/DbFitModuleTest_WithSQL/testDbFit.when.sql", 
			then = "data/DbFitModuleTest_WithSQL/testDbFit.then.wiki")
	public void testDbFit() {
	}

	@DbFit(when = "data/DbFitModuleTest_WithSQL/testDbFit.when.sql", then = "data/DbFitModuleTest_WithSQL/testDbFit.then.wiki")
	public void testDbFit2() {
	}

	@DbFit(vars = { @FitVar(key = "firstName", value = "darui.wu") })
	public void userSymbols() {
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void testRunSqlError() {

	}
}
