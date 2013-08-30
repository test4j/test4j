package org.test4j.json.encoder.single.fixed;

import java.io.StringWriter;
import java.util.ArrayList;

import org.junit.Test;
import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.single.fixed.DoubleEncoder;
import org.test4j.junit.annotations.DataFrom;

public class DoubleEncoderTest extends EncoderTest {

	@Test
	@DataFrom("double_data")
	public void testEncodeSingleValue(Double number, String expected) throws Exception {
		DoubleEncoder encoder = DoubleEncoder.instance;
		this.setUnmarkFeature(encoder);

		StringWriter writer = new StringWriter();
		encoder.encode(number, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	public static Object[][] double_data() {
		return new Object[][] { { 12.12D, "12.12" },// <br>
				{ 1000d, "1000" },// <br>
				{ null, "null" } // <br>
		};
	}
}
