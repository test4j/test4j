package org.test4j.json.encoder.single.fixed;

import java.util.ArrayList;

import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.single.fixed.EnumEncoder;
import org.test4j.json.helper.ClazzMap;
import org.test4j.json.helper.JSONFeature;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Test(groups = { "jtester", "json" })
public class EnumEncoderTest extends EncoderTest {

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
