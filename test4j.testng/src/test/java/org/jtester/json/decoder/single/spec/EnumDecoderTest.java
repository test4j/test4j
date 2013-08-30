package org.jtester.json.decoder.single.spec;

import org.jtester.json.JSON;
import org.jtester.json.helper.JSONFeature;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class EnumDecoderTest extends JTester {

	@Test
	public void testDecode() {
		String json = String.format("{'#class':'%s','#value':%s}", JSONFeature.class.getName(),
				JSONFeature.UnMarkClassFlag);

		Object o = JSON.toObject(json);
		want.object(o).clazIs(JSONFeature.class).isEqualTo(JSONFeature.UnMarkClassFlag);
	}

	@Test
	public void testDecode2() {
		String json = JSONFeature.UnMarkClassFlag.name();

		Object o = JSON.toObject(json, JSONFeature.class);
		want.object(o).clazIs(JSONFeature.class).isEqualTo(JSONFeature.UnMarkClassFlag);
	}
}
