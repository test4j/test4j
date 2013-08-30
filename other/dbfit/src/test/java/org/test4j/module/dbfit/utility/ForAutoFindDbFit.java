package org.test4j.module.dbfit.utility;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.annotations.DbFit.AUTO;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

/**
 * 非测试类，仅用于协助AutoFindDbFitTest测试类
 * 
 * @author darui.wudr
 * 
 */
@DbFit(when = "data/clazz.wiki", auto = AUTO.AUTO)
@Test(groups = "for-test")
public class ForAutoFindDbFit extends Test4J {

	@Test(groups = "test4j")
	@DbFit(then = "data/ForAutoFindDbFit/test_classwiki_autoLoader.wiki")
	public void test_classwiki_autoLoader() {

	}

	@DbFit
	public void test1() {

	}

	public void test2() {

	}

	@DbFit
	public void test3() {

	}

	public void test4() {

	}

	@DbFit(when = { "1.when.wiki", "2.when.wiki" }, then = { "1.then.wiki" })
	public void test5() {

	}
}
