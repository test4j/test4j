package org.jtester.module.dbfit.model;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.DbFit.AUTO;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
@DbFit(auto = AUTO.AUTO)
public class BigIntegerTest extends JTester {

	@Test
	public void testInsertBigInteger() {

	}

	@Test(groups = "oracle")
	public void testOracleBinDecimal() {

	}
}
