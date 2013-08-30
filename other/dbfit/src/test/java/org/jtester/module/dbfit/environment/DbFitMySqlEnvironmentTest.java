package org.jtester.module.dbfit.environment;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.DbFit.AUTO;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "mysql")
public class DbFitMySqlEnvironmentTest extends JTester {

	/**
	 * 在dbfit文件中测试mysql bigint数据类型
	 */
	@DbFit(auto = AUTO.AUTO)
	public void testBigInt() {

	}
}
