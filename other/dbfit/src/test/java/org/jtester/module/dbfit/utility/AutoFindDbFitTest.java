package org.jtester.module.dbfit.utility;

import java.lang.reflect.Method;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class AutoFindDbFitTest extends JTester {

	@Test
	public void testAutoFindMethodWhen_HasDBFit_FileExist() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test1");
		String[] whens = AutoFindDbFit.autoFindMethodWhen(ForAutoFindDbFit.class, m);
		want.array(whens).sizeEq(1).isEqualTo(new String[] { "data/ForAutoFindDbFit/test1.when.wiki" });
	}

	@Test
	public void testAutoFindMethodThen_HasDbFit_FileExist() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test1");
		String[] whens = AutoFindDbFit.autoFindMethodThen(ForAutoFindDbFit.class, m);
		want.array(whens).sizeEq(1).isEqualTo(new String[] { "data/ForAutoFindDbFit/test1.then.wiki" });
	}

	@Test
	public void testAutoFindMethodWhen_NoDBFit_FileUnExist() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test2");
		String[] whens = AutoFindDbFit.autoFindMethodWhen(ForAutoFindDbFit.class, m);
		want.array(whens).sizeEq(1).isEqualTo(new String[] { "data/ForAutoFindDbFit/test2.when.wiki" });
	}

	@Test
	public void testAutoFindMethodThen_NoDBFit_FileExist() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test2");
		String[] whens = AutoFindDbFit.autoFindMethodThen(ForAutoFindDbFit.class, m);
		want.array(whens).sizeEq(1).isEqualTo(new String[] { "data/ForAutoFindDbFit/test2.then.wiki" });
	}

	@Test
	public void testAutoFindMethodWhen_HasDBFit_FileUnExist() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test3");
		String[] whens = AutoFindDbFit.autoFindMethodWhen(ForAutoFindDbFit.class, m);
		want.array(whens).sizeEq(0);
	}

	@Test
	public void testAutoFindMethodThen_HasDBFit_FileUnExist() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test3");
		String[] thens = AutoFindDbFit.autoFindMethodThen(ForAutoFindDbFit.class, m);
		want.array(thens).sizeEq(0);
	}

	@Test
	public void testAutoFindMethodWhen_NoDBFit_FileUnExist2() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test4");
		String[] thens = AutoFindDbFit.autoFindMethodWhen(ForAutoFindDbFit.class, m);
		want.array(thens).sizeEq(0);
	}

	@Test
	public void testAutoFindMethodThen_NoDBFit_FileUnExist() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test4");
		String[] thens = AutoFindDbFit.autoFindMethodThen(ForAutoFindDbFit.class, m);
		want.array(thens).sizeEq(0);
	}

	@Test
	public void testAutoFindMethodWhen_HasDBFit_FileExist_AndDbFitWhenHasValue() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test5");
		String[] whens = AutoFindDbFit.autoFindMethodWhen(ForAutoFindDbFit.class, m);
		want.array(whens).sizeEq(3)
				.isEqualTo(new String[] { "1.when.wiki", "2.when.wiki", "data/ForAutoFindDbFit/test5.when.wiki" });
	}

	@Test
	public void testAutoFindMethodThen_HasDBFit_FileExist_AndDbFitThenHasValue() throws Exception {
		Method m = ForAutoFindDbFit.class.getMethod("test5");
		String[] thens = AutoFindDbFit.autoFindMethodThen(ForAutoFindDbFit.class, m);
		want.array(thens).sizeEq(2).isEqualTo(new String[] { "1.then.wiki", "data/ForAutoFindDbFit/test5.then.wiki" });
	}

	@Test
	public void testAutoFindClassWhen() {
		String[] wikis = AutoFindDbFit.autoFindClassWhen(ForAutoFindDbFit.class);
		want.array(wikis).sizeEq(2);
	}

	@Test
	public void testAutoFindClassWhen_UnAutoFind() {
		String[] wikis = AutoFindDbFit.autoFindClassWhen(ForAutoFindDbFit2.class);
		want.array(wikis).sizeEq(1);
	}
}
