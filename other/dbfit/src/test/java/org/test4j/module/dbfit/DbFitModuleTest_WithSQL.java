package org.test4j.module.dbfit;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.annotations.FitVar;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j" })
public class DbFitModuleTest_WithSQL extends Test4J {

	@DbFit(when = "org/test4j/module/dbfit/data/DbFitModuleTest_WithSQL/testDbFit.when.sql", 
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
