package org.jtester.hamcrest.matcher.string;

import java.util.Iterator;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "assertion")
public class StringNotBlankMatcherTest extends JTester {

	@Test(expectedExceptions = AssertionError.class, dataProvider = "dataForNotBlank")
	public void testNotBlank(String actual) {
		want.string(actual).notBlank();
	}

	@SuppressWarnings("rawtypes")
	@DataProvider
	public Iterator dataForNotBlank() {
		return new DataIterator() {
			{
				data((String) null);
				data("");
				data(" ");
				data("\n");
			}
		};
	}

	public void testNotBlank_Success() {
		want.string("tt").notBlank();
	}
}
