package org.test4j.json.encoder.single.fixed;

import java.math.BigInteger;

import org.junit.Test;
import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.single.fixed.BigIntegerEncoder;

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
