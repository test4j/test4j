package org.jtester.hamcrest;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class TestStringAssert extends JTester {
	@Test
	public void test11() {
		want.string("ddd").contains("d").contains("d");
		want.string("ddd").isEqualTo("ddd");
		want.string("ddd").eqIgnoreCase("dDD");
		want.string("eeeed").end("ed");
		want.string("eeeed").end("ed").start("eee");
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void test12() {
		want.string("ddd").contains("de").contains("d");
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void test13() {
		want.string("ddd").contains("d").contains("de");
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void test21() {
		want.string("abcd").any(the.string().contains("ad"), the.string().contains("de"));
	}

	@Test
	public void test22() {
		want.string("abcd").any(the.string().contains("ab"), the.string().contains("cd"));
	}

	@Test
	public void test31() {
		want.string("abcd").all(the.string().contains("ab"), the.string().contains("cd"));
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void test32() {
		want.string("abcd").all(the.string().contains("ad"), the.string().contains("cd"));
	}

	public void eqIgnorBlank() {
		want.string(" ab ").eqIgnoreSpace("ab");
		want.string("abC").eqIgnoreCase("aBc");
	}
}
