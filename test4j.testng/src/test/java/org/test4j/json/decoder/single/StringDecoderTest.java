package org.test4j.json.decoder.single;

import org.test4j.json.JSON;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class StringDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		String str = JSON.toObject("test string", String.class);
		want.string(str).isEqualTo("test string");
	}
}
