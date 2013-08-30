package org.jtester.json.encoder.single.fixed;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class DoubleEncoderTest extends EncoderTest {

	@Test(dataProvider = "double_data")
	public void testEncodeSingleValue(Double number, String expected) throws Exception {
		DoubleEncoder encoder = DoubleEncoder.instance;
		this.setUnmarkFeature(encoder);

		StringWriter writer = new StringWriter();
		encoder.encode(number, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	@DataProvider
	public Object[][] double_data() {
		return new Object[][] { { 12.12D, "12.12" },// <br>
				{ 1000d, "1000" },// <br>
				{ null, "null" } // <br>
		};
	}
}
