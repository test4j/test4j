package org.jtester.module.dbfit.environment;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class MySQLTypeMapTest extends JTester {

	@DbFit(when = "mysqltype.when.wiki", then = "mysqltype.then.wiki")
	public void testMySqlType() {

	}
}
