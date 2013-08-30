package org.jtester.tools.commons;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ListHelper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
@Test(groups = "jtester")
public class ListHelperTest extends JTester {

	@Test
	public void testToList() {
		List list = ListHelper.toList(1, 2, 3);
		want.collection(list).reflectionEq(new Integer[] { 1, 2, 3 });
	}

	@Test(dataProvider = "testToList_data")
	public void testToList_Object(Object input, List output) {
		List list = ListHelper.toList(input);
		want.collection(list).reflectionEq(output);
	}

	@DataProvider
	public Object[][] testToList_data() {
		return new Object[][] { { Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3) },// <br>
				{ new Integer[] { 1, 2, 3 }, Arrays.asList(1, 2, 3) }, // <br>
				{ 1, Arrays.asList(1) }, // <br>
				{ null, Arrays.asList((Object) null) }, // <br>
				{ new Integer[] { 1, 2, 3 }, Arrays.asList(1, 2, 3) } // <br>
		};
	}

	public void testToListMulti() {
		List list = ListHelper.toList(1, 2, 3);
		want.collection(list).sizeEq(3).hasAllItems(1, 2, 3);

		list = ListHelper.toList();
		want.collection(list).sizeEq(0);
	}

	public void testToList_WithMap() {
		List list = ListHelper.toList(new HashMap() {
			{
				this.put(1, 1);
				this.put(2, 2);
				this.put(3, 3);
			}
		}, true);
		want.collection(list).sizeEq(3).reflectionEq(toArray(1, 2, 3), EqMode.IGNORE_ORDER);
	}
}
