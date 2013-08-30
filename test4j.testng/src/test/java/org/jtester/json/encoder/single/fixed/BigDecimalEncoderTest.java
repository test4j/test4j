package org.jtester.json.encoder.single.fixed;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jtester.json.encoder.EncoderTest;
import org.testng.annotations.Test;

@Test
public class BigDecimalEncoderTest extends EncoderTest {

	@Test
	public void testEncode() {
		String bigStr = "99999999999999999.999999";
		BigDecimal bigdec = new BigDecimal(bigStr);

		BigDecimalEncoder encoder = BigDecimalEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(bigdec, writer, new ArrayList<String>());
		String json = writer.toString();
		want.string(json).isEqualTo(bigStr);
	}
}
