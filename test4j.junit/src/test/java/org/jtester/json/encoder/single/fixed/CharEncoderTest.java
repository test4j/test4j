package org.jtester.json.encoder.single.fixed;

import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class CharEncoderTest extends EncoderTest {

	@Test
	@DataFrom("char_data")
	public void testEncodeSingleValue(char ch, String expected) throws Exception {
		CharEncoder encoder = CharEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(ch, writer, new ArrayList<String>());
		String result = writer.toString();
		want.string(result).isEqualTo(expected);
	}

	public static Object[][] char_data() {
		return new Object[][] { { 'a', "'a'" },// <br>
				{ '"', "'\\\"'" },// <br>
				{ '\'', "'\\\''" },// <br>
				{ '\n', "'\\n'" } // <br>
		};
	}
}
