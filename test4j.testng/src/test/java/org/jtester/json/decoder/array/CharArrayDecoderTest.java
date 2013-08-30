package org.jtester.json.decoder.array;

import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

public class CharArrayDecoderTest extends JTester {

	@Test(groups = { "jtester", "json" })
	public void testParseFromJSONArray() {
		String json = "['a',b,\"c\"]";
		Character[] ints = JSON.toObject(json, char[].class);
		want.array(ints).sizeEq(3).reflectionEq(new char[] { 'a', 'b', 'c' });
	}
}
