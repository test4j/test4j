package org.jtester.hamcrest.iassert.object.impl;

import java.util.Iterator;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class CollectionAssertTest_HasItem_DataProvider extends JTester {

	@SuppressWarnings("rawtypes")
	@DataProvider(name = "provide_hasitems")
	public Iterator provideArray() {
		return new DataIterator() {
			{
				data(new Integer[] { 1, 2, 3 }, 1, new Integer[] { 2 });
				data(new Character[] { 'a', 'b', 'c' }, 'a', new Character[] { 'b' });
				data(new Boolean[] { true, false }, true, null);
				data(new Double[] { 1.2d, 2.8d, 3.9d }, 1.2d, new Double[] { 3.9d });
			}
		};
	}

	@Test(dataProvider = "provide_hasitems")
	public void hasItems(Object[] actual, Object firstExpected, Object[] expected) {
		want.array(actual).hasAllItems(firstExpected, expected);
	}
}
