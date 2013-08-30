package org.jtester.json.decoder.array;

import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class BooleanArrayDecoderTest extends JTester {

	@Test
	public void testParseFromJSONArray() {
		String json = "[1,false,'true',true]";
		Boolean[] ints = JSON.toObject(json, boolean[].class);
		want.array(ints).sizeEq(4).reflectionEq(new boolean[] { true, false, true, true });
	}
}
