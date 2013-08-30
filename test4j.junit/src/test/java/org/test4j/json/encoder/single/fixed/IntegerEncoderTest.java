package org.test4j.json.encoder.single.fixed;

import java.io.StringWriter;
import java.util.ArrayList;

import org.junit.Test;
import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.single.fixed.IntegerEncoder;
import org.test4j.junit.annotations.DataFrom;

public class IntegerEncoderTest extends EncoderTest {

	@Test
	@DataFrom("int_data")
	public void testEncodeSingleValue(Integer number, String expected) throws Exception {
		IntegerEncoder encoder = IntegerEncoder.instance;
		this.setUnmarkFeature(encoder);

		StringWriter writer = new StringWriter();
		encoder.encode(number, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	public static Object[][] int_data() {
		return new Object[][] { { 1212, "1212" },// <br>
				{ null, "null" } // <br>
		};
	}
}
