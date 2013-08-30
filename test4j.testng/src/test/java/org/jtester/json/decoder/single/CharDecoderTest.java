package org.jtester.json.decoder.single;

import org.jtester.testng.JTester;
import org.test4j.json.JSON;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class CharDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		Character ch = JSON.toObject("'a'", Character.class);
		want.character(ch).isEqualTo('a');
	}
}
