package org.jtester.json.encoder.single.spec;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jtester.json.encoder.single.spec.SimpleDateFormatEncoder;
import org.jtester.json.helper.JSONFeature;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Test(groups = { "jtester", "json" })
public class SimpleDateFormatEncoderTest extends JTester {

	public void testEncode() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormatEncoder encoder = SimpleDateFormatEncoder.instance;
		encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

		StringWriter writer = new StringWriter();
		encoder.encode(df, writer, new ArrayList<String>());

		String json = writer.toString();
		want.string(json).isEqualTo("'yyyy-MM-dd'");
	}
}
