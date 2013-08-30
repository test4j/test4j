package org.test4j.json.decoder.array;

import org.test4j.json.JSON;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class ByteArrayDecoderTest extends JTester {

	@Test
	public void testParseFromJSONArray() {
		String json = "['1','0',1]";
		Byte[] ints = JSON.toObject(json, byte[].class);
		want.array(ints).sizeEq(3).reflectionEq(new byte[] { 1, 0, 1 });
	}
}
