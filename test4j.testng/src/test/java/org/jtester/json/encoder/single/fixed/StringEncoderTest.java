package org.jtester.json.encoder.single.fixed;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class StringEncoderTest extends EncoderTest {

	@Test(dataProvider = "stringJsonData")
	public void testEncode(String value, String json) throws Exception {
		StringEncoder encoder = StringEncoder.instance;
		this.setUnmarkFeature(encoder);

		StringWriter writer = new StringWriter();
		encoder.encode(value, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(json);
	}

	@DataProvider
	public Object[][] stringJsonData() {
		return new Object[][] { { "value", "'value'" },// <br>
				{ null, "null" }, // <br>
				{ "\n\t", "'\\n\\t'" }, // <br>
				{ "'", "'\\''" }, // <br>
				{ "\"", "'\\\"'" }, // <br>
				{ "\f\b", "'\\f\\b'" }, // <br>
				{ "\t Tab符", "'\\t Tab符'" }, // <br>
				{ "\\u2345乱码", "'\\\\u2345乱码'" } // <br>
		};
	}
}
