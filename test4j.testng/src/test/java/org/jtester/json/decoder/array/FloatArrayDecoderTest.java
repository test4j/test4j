package org.jtester.json.decoder.array;

import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

public class FloatArrayDecoderTest extends JTester {

	@Test(groups = { "jtester", "json" })
	public void testParseFromJSONArray() {
		String json = "['12',12.4,\"45.6f\"]";
		Float[] ints = JSON.toObject(json, float[].class);
		want.array(ints).sizeEq(3).reflectionEq(new float[] { 12f, 12.4f, 45.6f });
	}
}
