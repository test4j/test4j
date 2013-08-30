package org.jtester.json.encoder.single.fixed;

import java.util.ArrayList;
import java.util.UUID;

import org.jtester.json.encoder.EncoderTest;
import org.jtester.json.helper.JSONFeature;
import org.junit.Test;

public class UUIDEncoderTest extends EncoderTest {

	@Test
	public void testEncodeSingleValue() throws Exception {
		UUID uuid = UUID.randomUUID();
		UUIDEncoder encoder = UUIDEncoder.instance;
		encoder.setFeatures(JSONFeature.UseSingleQuote);

		encoder.encode(uuid, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).start("{#class:'UUID',#value:'").end("'}");
	}
}
