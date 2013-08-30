package org.jtester.json.decoder.single;

import java.io.File;
import java.net.URI;

import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class URIDecoderTest extends JTester {

	@Test
	public void testDecodeSimpleValue() {
		String json = "{'#class':'URI','#value':'file:/d:/path/1.txt'}";
		URI uri = JSON.toObject(json);
		want.object(uri).isEqualTo(new File("d:/path/1.txt").toURI());
	}
}
