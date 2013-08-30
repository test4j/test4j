package org.jtester.json.encoder.array;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.helper.JSONFeature;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Test(groups = { "jtester", "json" })
public class ShortArrayEncoderTest extends JTester {
	@Test
	public void testEncode() throws Exception {
		short[] values = new short[] { 1234, Short.valueOf("4567") };

		JSONEncoder encoder = JSONEncoder.get(values.getClass());

		encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

		StringWriter writer = new StringWriter();
		encoder.encode(values, writer, new ArrayList<String>());
		String json = writer.toString();
		want.string(json).eqIgnoreSpace("[1234, 4567]");
	}

	@Test
	public void testEncode_Short() throws Exception {
		Short[] values = new Short[] { 1234, null };

		JSONEncoder encoder = JSONEncoder.get(values.getClass());
		want.object(encoder).clazIs(ObjectArrayEncoder.class);

		encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

		StringWriter writer = new StringWriter();
		encoder.encode(values, writer, new ArrayList<String>());
		String json = writer.toString();
		want.string(json).eqIgnoreSpace("[1234, null]");
	}
}
