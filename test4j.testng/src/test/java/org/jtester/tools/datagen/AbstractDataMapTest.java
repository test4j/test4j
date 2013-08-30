package org.jtester.tools.datagen;

import java.util.Arrays;
import java.util.Iterator;

import org.jtester.json.ITypeConverter;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "serial", "rawtypes" })
public class AbstractDataMapTest extends JTester {

	@Test(dataProvider = "dataForConvert")
	public void testConvert(final Object value, Object expected) {
		final String key = "my-test-key";
		DataMap map = new DataMap() {

			{
				this.put(key, value);
			}
		};
		map.convert(key, ITypeConverter.defaultConverter);
		Object list = map.get(key);
		want.object(list).reflectionEq(expected);
	}

	@DataProvider
	public Iterator dataForConvert() {
		return new DataIterator() {

			{
				data("true", true);
				data("[1,2,3]", Arrays.asList(1, 2, 3));
				data("[true,true,false]", new Boolean[] { true, true, false });
			}
		};
	}
}
