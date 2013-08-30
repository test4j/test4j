package org.test4j.module.dbfit.fixture;

import org.test4j.testng.utility.TestNgUtil;

public class DbUnitTestFixture extends DatabaseFixture {
	public boolean testng(String clazz, String method) {
		return TestNgUtil.run(clazz, method, false);
	}
}
