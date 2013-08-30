package org.test4j.json.encoder.single.fixed;

import java.util.ArrayList;

import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.single.fixed.CharEncoder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class CharEncoderTest extends EncoderTest {

	@Test(dataProvider = "char_data")
	public void testEncodeSingleValue(char ch, String expected) throws Exception {
		CharEncoder encoder = CharEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(ch, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	@DataProvider
	public Object[][] char_data() {
		return new Object[][] { { 'a', "'a'" },// <br>
				{ '"', "'\\\"'" },// <br>
				{ '\'', "'\\\''" },// <br>
				{ '\n', "'\\n'" } // <br>
		};
	}
}
