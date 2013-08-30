package org.jtester.module.dbfit.fixture;

import org.jtester.testng.utility.TestNgUtil;

public class DbUnitTestFixture extends DatabaseFixture {
	public boolean testng(String clazz, String method) {
		return TestNgUtil.run(clazz, method, false);
	}
}
