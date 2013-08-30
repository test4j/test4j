package org.jtester.module.dbfit.utility;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.DbFit.AUTO;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

/**
 * 非测试类，仅用于协助AutoFindDbFitTest测试类
 * 
 * @author darui.wudr
 * 
 */
@DbFit(when = "data/clazz.wiki", auto = AUTO.AUTO)
@Test(groups = "for-test")
public class ForAutoFindDbFit extends JTester {

	@Test(groups = "jtester")
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
