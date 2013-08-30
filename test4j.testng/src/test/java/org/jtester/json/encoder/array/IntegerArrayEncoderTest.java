package org.jtester.json.encoder.array;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.helper.JSONFeature;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Test(groups = { "jtester", "json" })
public class IntegerArrayEncoderTest extends JTester {
	@Test
	public void testEncode() throws Exception {
		int[] ints = new int[] { 1, 2, 3 };

		JSONEncoder encoder = JSONEncoder.get(ints.getClass());
		encoder.setFeatures(JSONFeature.UnMarkClassFlag);
		StringWriter writer = new StringWriter();
		encoder.encode(ints, writer, new ArrayList<String>());
		String json = writer.toString();
		want.string(json).eqIgnoreSpace("[1,2,3]");
	}

	@Test
	public void testEncode_Integer() throws Exception {
		Integer[] ints = new Integer[] { 1, null, 3 };

		JSONEncoder encoder = JSONEncoder.get(ints.getClass());
		encoder.setFeatures(JSONFeature.UnMarkClassFlag);
		want.object(encoder).clazIs(ObjectArrayEncoder.class);

		StringWriter writer = new StringWriter();
		encoder.encode(ints, writer, new ArrayList<String>());
		String json = writer.toString();
		want.string(json).eqIgnoreSpace("[1,null,3]");
	}
}
