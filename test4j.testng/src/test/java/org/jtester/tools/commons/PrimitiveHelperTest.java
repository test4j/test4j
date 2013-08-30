package org.jtester.tools.commons;

import java.util.Iterator;

import org.jtester.testng.JTester;
import org.jtester.tools.commons.PrimitiveHelper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
@Test(groups = { "jtester", "utility" })
public class PrimitiveHelperTest extends JTester {

	@Test(dataProvider = "testDoesEqualData")
	public void testDoesEqual(Number num1, Number num2, boolean result) {
		boolean actual = PrimitiveHelper.doesEqual(num1, num2);
		want.bool(actual).is(result);
	}

	@DataProvider
	public Iterator testDoesEqualData() {
		return new DataIterator() {
			{
				data(1, 1L, true);
				data(Integer.valueOf(2), 2L, true);
				data(Long.valueOf(3), Short.valueOf("3"), true);
				data(4, 4.0, false);
				data(5.0d, 5.0f, true);
			}
		};
	}
}
