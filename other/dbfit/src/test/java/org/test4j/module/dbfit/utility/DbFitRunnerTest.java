package org.test4j.module.dbfit.utility;

import java.util.HashMap;
import java.util.Map;

import org.test4j.module.dbfit.DbFitModuleTest;
import org.test4j.module.dbfit.IDbFit;
import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.utility.DbFitRunner;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "dbfit" })
public class DbFitRunnerTest extends Test4J implements IDbFit {
	@Test
	@DbFit(when = "org/test4j/module/dbfit/utility/SimpleQuery_Init.wiki")
	public void runTest_HasName() throws Exception {
		DbFitRunner tdd = new DbFitRunner("test-output");

		tdd.runDbFitTest(DbFitRunnerTest.class, "SimpleQuery.wiki");
	}

	@Test
	@DbFit(when = "org/test4j/module/dbfit/utility/SimpleQuery_Init.wiki")
	public void runTest() throws Exception {
		fit.runDbFit(null, "org/test4j/module/dbfit/utility/SimpleQuery.wiki");
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
	@DbFit(when = "org/test4j/module/dbfit/utility/SimpleQuery_Init.wiki")
	public void test_WikiFileHasVariable() throws Exception {
		fit.setSymbol("first_name1", "aaa");
		fit.setSymbol("first_name2", "bbb");
		fit.runDbFit(null, "org/test4j/module/dbfit/utility/SimpleQuery_userVar.wiki");
	}
}
