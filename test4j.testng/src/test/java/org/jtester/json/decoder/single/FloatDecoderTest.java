package org.jtester.json.decoder.single;

import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class FloatDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		Float f = JSON.toObject("12.34f", Float.class);
		want.number(f).isEqualTo(12.34f);
	}
}
