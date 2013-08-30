package org.jtester.json.encoder.single.fixed;

import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class BooleanEncoderTest extends EncoderTest {

	@Test
	@DataFrom("boolean_data")
	public void testEncodeSingleValue(Boolean value, String expected) throws Exception {
		BooleanEncoder encoder = BooleanEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(value, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	public static Object[][] boolean_data() {
		return new Object[][] { { true, "true" },// <br>
				{ false, "false" },// <br>
				{ null, "null" } // <br>
		};
	}
}
