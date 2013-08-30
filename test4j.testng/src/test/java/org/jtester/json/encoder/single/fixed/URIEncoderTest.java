package org.jtester.json.encoder.single.fixed;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.jtester.json.helper.JSONFeature;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class URIEncoderTest extends EncoderTest {

	@Test
	public void testEncodeSingleValue() throws Exception {
		File file = new File("d:\\path\\1.txt");
		URI uri = file.toURI();
		URIEncoder encoder = URIEncoder.instance;
		encoder.setFeatures(JSONFeature.UseSingleQuote);

		encoder.encode(uri, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo("{#class:'URI',#value:'file:/d:/path/1.txt'}");
	}
}
