package org.jtester.datafilling.common;

import java.lang.reflect.Type;
import java.util.Map;

import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.model.GenericPojo;
import org.jtester.module.ICore;
import org.junit.Test;

@SuppressWarnings("serial")
public class AttributeInfoTest implements ICore {

	@Test
	public void testExactArgsTypeMap() throws Exception {
		Map<String, Type> typesMap = new AttributeInfo(GenericPojo.class).setAttrArgs(String.class, Integer.class)
				.getArgsTypeMap();
		want.map(typesMap).sizeEq(2).reflectionEq(new DataMap() {
			{
				this.put("F", String.class);
				this.put("S", Integer.class);
			}
		});
	}
}
