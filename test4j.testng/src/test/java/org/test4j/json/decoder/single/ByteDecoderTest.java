package org.test4j.json.decoder.single;

import org.test4j.json.JSON;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class ByteDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		Byte b = JSON.toObject("1", Byte.class);
		want.bite(b).isEqualTo(Byte.valueOf("1"));
	}
}
