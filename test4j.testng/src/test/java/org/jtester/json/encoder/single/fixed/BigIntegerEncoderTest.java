package org.jtester.json.encoder.single.fixed;

import java.math.BigInteger;

import org.jtester.json.encoder.EncoderTest;
import org.testng.annotations.Test;

public class BigIntegerEncoderTest extends EncoderTest {

	@Test
	public void testBigIntegerEncoder() {
		String intStr = "9999999999999999999999999";
		BigInteger bigint = new BigInteger(intStr);

		BigIntegerEncoder encoder = BigIntegerEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(bigint, writer, references);
		String json = writer.toString();
		want.string(json).isEqualTo(intStr);
	}
}
