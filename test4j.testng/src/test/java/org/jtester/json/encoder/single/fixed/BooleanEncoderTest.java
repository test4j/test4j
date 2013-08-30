package org.jtester.json.encoder.single.fixed;

import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class BooleanEncoderTest extends EncoderTest {

	@Test(dataProvider = "boolean_data")
	public void testEncodeSingleValue(Boolean value, String expected) throws Exception {
		BooleanEncoder encoder = BooleanEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(value, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	@DataProvider
	public Object[][] boolean_data() {
		return new Object[][] { { true, "true" },// <br>
				{ false, "false" },// <br>
				{ null, "null" } // <br>
		};
	}
}
