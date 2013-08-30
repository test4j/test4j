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
@DbFit(when = "data/clazz.wiki", auto = AUTO.UN_AUTO)
@Test(groups = "for-test")
public class ForAutoFindDbFit2 extends Test4J {

	@Test(groups = "test4j", description = "在这个例子中class不加载ForAutoFindDbFit2_wiki文件")
	@DbFit(then = "data/ForAutoFindDbFit2/unauto_load_wiki.wiki")
	public void test_unautoLoad() {

	}

	@DbFit
	public void test1() {

	}

	@DbFit(when = { "1.wiki", "2.wiki" }, auto = AUTO.AUTO)
	public void test2() {

	}
}
