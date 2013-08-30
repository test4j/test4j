package org.jtester.json.encoder.single.fixed;

import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.jtester.json.helper.ClazzMap;
import org.jtester.json.helper.JSONFeature;
import org.junit.Test;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EnumEncoderTest extends EncoderTest {
	@Test
	public void testEncode() throws Exception {
		Enum value = JSONFeature.UnMarkClassFlag;
		EnumEncoder encoder = EnumEncoder.instance;
		encoder.setFeatures(JSONFeature.UseSingleQuote);

		encoder.encode(value, writer, new ArrayList<String>());
		String json = writer.toString();
		String expected = String.format("{#class:'%s%s',#value:%s}", JSONFeature.class.getName(),
				ClazzMap.getReferenceAddress(JSONFeature.UnMarkClassFlag), JSONFeature.UnMarkClassFlag.name());
		want.string(json).eqIgnoreSpace(expected);
	}
}
