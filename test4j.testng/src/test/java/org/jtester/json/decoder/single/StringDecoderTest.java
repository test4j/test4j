package org.jtester.json.decoder.single;

import org.jtester.testng.JTester;
import org.test4j.json.JSON;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class StringDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		String str = JSON.toObject("test string", String.class);
		want.string(str).isEqualTo("test string");
	}
}
