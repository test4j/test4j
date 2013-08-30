package org.jtester.json.encoder.single.fixed;

import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class ByteEncoderTest extends EncoderTest {

	@Test(dataProvider = "byte_data")
	public void testEncodeSingleValue(Byte value, String expected) throws Exception {
		ByteEncoder encoder = ByteEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(value, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	@DataProvider
	public Object[][] byte_data() {
		return new Object[][] { { Byte.valueOf("0110"), "110" }, // <br>
				{ null, "null" } // <br>
		};
	}
}
