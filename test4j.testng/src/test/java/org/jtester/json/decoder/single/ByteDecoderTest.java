package org.jtester.json.decoder.single;

import org.jtester.testng.JTester;
import org.test4j.json.JSON;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class ByteDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		Byte b = JSON.toObject("1", Byte.class);
		want.bite(b).isEqualTo(Byte.valueOf("1"));
	}
}
