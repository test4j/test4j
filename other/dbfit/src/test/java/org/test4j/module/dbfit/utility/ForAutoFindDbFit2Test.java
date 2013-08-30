package org.test4j.module.dbfit.utility;

import java.lang.reflect.Method;

import org.test4j.module.dbfit.utility.AutoFindDbFit;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class ForAutoFindDbFit2Test extends Test4J {
	@Test(description = "class上定义了禁止自动查找_方法上未覆盖定义")
	public void test_when() throws Exception {
		Method m = ForAutoFindDbFit2.class.getMethod("test1");
		String[] wikis = AutoFindDbFit.autoFindMethodWhen(ForAutoFindDbFit2.class, m);

		want.array(wikis).sizeEq(0);
	}

	@Test(description = "class上定义了禁止自动查找_方法上未覆盖定义")
	public void test_then() throws Exception {
		Method m = ForAutoFindDbFit2.class.getMethod("test1");
		String[] wikis = AutoFindDbFit.autoFindMethodThen(ForAutoFindDbFit2.class, m);

		want.array(wikis).sizeEq(0);
	}

	@Test(description = "class上定义了禁止自动查找_方法上覆盖定义")
	public void test_when2() throws Exception {
		Method m = ForAutoFindDbFit2.class.getMethod("test2");
		String[] wikis = AutoFindDbFit.autoFindMethodWhen(ForAutoFindDbFit2.class, m);

		want.array(wikis).sizeEq(3);
	}

	@Test(description = "class上定义了禁止自动查找_方法上覆盖定义")
	public void test_then2() throws Exception {
		Method m = ForAutoFindDbFit2.class.getMethod("test2");
		String[] wikis = AutoFindDbFit.autoFindMethodThen(ForAutoFindDbFit2.class, m);

		want.array(wikis).sizeEq(1);
	}
}
