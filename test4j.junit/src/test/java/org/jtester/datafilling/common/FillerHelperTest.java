package org.jtester.datafilling.common;

import java.lang.reflect.Type;

import org.jtester.datafilling.FillUp;
import org.jtester.module.ICore;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class FillerHelperTest implements ICore {

	@Test
	public void testGetFillUpType() throws Exception {
		FillUp fillup = new FillUp<String>() {
		};

		Class clazz = FillerHelper.getFillUpType(fillup);
		want.object(clazz).eq(String.class);
	}

	@Test
	public void testGetFillUpArgs() throws Exception {
		FillUp fillup = new FillUp<String>(String.class, int.class) {
		};

		Type[] types = fillup.getArgTypes();
		want.array(types).reflectionEq(new Object[] { String.class, int.class });
	}
}
