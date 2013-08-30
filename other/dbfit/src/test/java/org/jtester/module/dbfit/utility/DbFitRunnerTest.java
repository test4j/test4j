package org.jtester.module.dbfit.utility;

import java.util.HashMap;
import java.util.Map;

import org.jtester.module.dbfit.DbFitModuleTest;
import org.jtester.module.dbfit.IDbFit;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.utility.DbFitRunner;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "dbfit" })
public class DbFitRunnerTest extends JTester implements IDbFit {
	@Test
	@DbFit(when = "org/jtester/module/dbfit/utility/SimpleQuery_Init.wiki")
	public void runTest_HasName() throws Exception {
		DbFitRunner tdd = new DbFitRunner("test-output");

		tdd.runDbFitTest(DbFitRunnerTest.class, "SimpleQuery.wiki");
	}

	@Test
	@DbFit(when = "org/jtester/module/dbfit/utility/SimpleQuery_Init.wiki")
	public void runTest() throws Exception {
		fit.runDbFit(null, "org/jtester/module/dbfit/utility/SimpleQuery.wiki");
	}

	@Test
	public void testPrepareData() {
		Map<String, String> symbols = new HashMap<String, String>();
		symbols.put("name", "darui.wu");
		symbols.put("myid", "2");

		fit.runDbFit(DbFitModuleTest.class, symbols, "DbFitModuleTest.exactFitVar.when.wiki",
				"DbFitModuleTest.exactFitVar.then.wiki");
	}

	@Test(description = "测试wiki文件query中使用变量")
	@DbFit(when = "org/jtester/module/dbfit/utility/SimpleQuery_Init.wiki")
	public void test_WikiFileHasVariable() throws Exception {
		fit.setSymbol("first_name1", "aaa");
		fit.setSymbol("first_name2", "bbb");
		fit.runDbFit(null, "org/jtester/module/dbfit/utility/SimpleQuery_userVar.wiki");
	}
}
