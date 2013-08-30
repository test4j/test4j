package org.test4j.json.decoder.single;

import org.test4j.json.JSON;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;


@Test(groups = { "jtester", "json" })
public class CharDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		Character ch = JSON.toObject("'a'", Character.class);
		want.character(ch).isEqualTo('a');
	}
}
