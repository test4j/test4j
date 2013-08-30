package org.jtester.json.decoder.array;

import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

public class ShortArrayDecoderTest extends JTester {

	@Test(groups = { "jtester", "json" })
	public void testParseFromJSONArray() {
		String json = "['12',124,\"456\"]";
		Short[] ints = JSON.toObject(json, short[].class);
		want.array(ints).sizeEq(3).reflectionEq(new short[] { 12, 124, 456 });
	}
}
