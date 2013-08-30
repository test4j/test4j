package org.test4j.json.encoder.single.fixed;

import java.util.ArrayList;

import org.junit.Test;
import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.single.fixed.ByteEncoder;
import org.test4j.junit.annotations.DataFrom;

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
