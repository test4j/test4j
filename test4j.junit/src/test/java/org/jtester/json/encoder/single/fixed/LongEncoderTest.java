package org.jtester.json.encoder.single.fixed;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class LongEncoderTest extends EncoderTest {

	@Test
	@DataFrom("long_data")
	public void testEncodeSingleValue(Long number, String expected) throws Exception {
		LongEncoder encoder = LongEncoder.instance;
		this.setUnmarkFeature(encoder);

		StringWriter writer = new StringWriter();
		encoder.encode(number, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	public static Object[][] long_data() {
		return new Object[][] { { 1212L, "1212" },// <br>
				{ 1000l, "1000" },// <br>
				{ null, "null" } // <br>
		};
	}
}
