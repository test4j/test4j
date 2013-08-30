package org.jtester.json.encoder.single.fixed;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class StringEncoderTest extends EncoderTest {

	@Test
	@DataFrom("stringJsonData")
	public void testEncode(String value, String json) throws Exception {
		StringEncoder encoder = StringEncoder.instance;
		this.setUnmarkFeature(encoder);

		StringWriter writer = new StringWriter();
		encoder.encode(value, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(json);
	}

	public static Object[][] stringJsonData() {
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
