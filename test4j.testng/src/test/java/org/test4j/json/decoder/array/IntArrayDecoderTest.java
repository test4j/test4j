package org.test4j.json.decoder.array;

import org.test4j.json.JSON;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class IntArrayDecoderTest extends JTester {

	@Test
	public void testParseFromJSONArray() {
		String json = "['1','2','3']";
		Integer[] ints = JSON.toObject(json, int[].class);
		want.array(ints).sizeEq(3).reflectionEq(new int[] { 1, 2, 3 });
	}

}
