package org.test4j.json.decoder.single;

import org.test4j.json.JSON;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class LongDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		Long l = JSON.toObject("1234L", Long.class);
		want.number(l).isEqualTo(1234L);
	}

}
