package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.math.BigInteger;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class BigIntegerEncoder extends FixedTypeEncoder<BigInteger> {

	public static BigIntegerEncoder instance = new BigIntegerEncoder();

	protected BigIntegerEncoder() {
		super(BigInteger.class);
	}

	@Override
	protected void encodeSingleValue(BigInteger target, Writer writer) throws Exception {
		writer.append(target.toString());
	}
}
