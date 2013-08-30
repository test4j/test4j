package org.jtester.json;

import org.jtester.json.ITypeConverter;
import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

public class ArrayConverterTest extends JTester {
	Boolean[] boolArray;

	@Test(groups = "convert")
	public void testConvert() throws Exception {
		ITypeConverter converter = ITypeConverter.defaultConverter;
		Object[] arr = converter.convert("[null,true,false]");
		want.array(arr).reflectionEq(new Boolean[] { null, true, false });
	}

	@Test(groups = "convert")
	public void testConvert_Array2() throws Exception {
		ITypeConverter converter = new ITypeConverter() {
			public <T> T convert(Object from) {
				return JSON.toObject((String) from, Boolean[][].class);
			}

			public boolean accept(Object value) {
				return true;
			}
		};
		Object arr = converter.convert("[[true,false],[true,false]]");
		want.object(arr).reflectionEq(new Boolean[][] { { true, false }, { true, false } });
	}
}
