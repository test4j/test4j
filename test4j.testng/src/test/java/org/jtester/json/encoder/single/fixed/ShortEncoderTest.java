package org.jtester.json.encoder.single.fixed;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class ShortEncoderTest extends EncoderTest {

	@Test(dataProvider = "short_data")
	public void testEncodeSingleValue(Short number, String expected) throws Exception {
		ShortEncoder encoder = ShortEncoder.instance;
		this.setUnmarkFeature(encoder);

		StringWriter writer = new StringWriter();
		encoder.encode(number, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	@DataProvider
	public Object[][] short_data() {
		return new Object[][] { { Short.valueOf("45"), "45" },// <br>
				{ null, "null" } // <br>
		};
	}
}
