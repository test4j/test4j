package org.test4j.module.dbfit;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.annotations.DbFit.AUTO;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class DbFitModuleTest_UsageDataSource extends Test4J {

	@DbFit(dataSource = "test4j_another", auto = AUTO.AUTO)
	public void testDbFit() {

	}
}
