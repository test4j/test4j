package org.jtester.json.encoder.single.fixed;

import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class ByteEncoderTest extends EncoderTest {

	@Test
	@DataFrom("byte_data")
	public void testEncodeSingleValue(Byte value, String expected) throws Exception {
		ByteEncoder encoder = ByteEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(value, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	public static Object[][] byte_data() {
		return new Object[][] { { Byte.valueOf("0110"), "110" }, // <br>
				{ null, "null" } // <br>
		};
	}
}
