package org.test4j.datafilling.common;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Test;
import org.test4j.datafilling.common.AttributeInfo;
import org.test4j.datafilling.model.GenericPojo;
import org.test4j.module.ICore;

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
