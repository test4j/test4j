package org.test4j.spec.util;

import java.util.Iterator;

import org.test4j.spec.util.ParaConverter;
import org.test4j.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
public class ParaConverterTest extends JTester {

	@Test(dataProvider = "dataForGetStringValue")
	public void testGetStringValue(String input, String expected) {
		String result = ParaConverter.getStringValue(input);
		want.string(result).isEqualTo(expected);
	}

	@DataProvider
	Iterator dataForGetStringValue() {
		return new DataIterator() {
			{
				data(null, null);
				data("abc", "abc");
				data("\"abc\"", "abc");
				data("\"abc", "\"abc");
				data("'abc'", "abc");
				data("'abc", "'abc");
				data("\"abc'", "\"abc'");
			}
		};
	}
}
