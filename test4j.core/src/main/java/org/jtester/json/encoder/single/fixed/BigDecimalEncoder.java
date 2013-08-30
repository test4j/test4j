package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.math.BigDecimal;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class BigDecimalEncoder extends FixedTypeEncoder<BigDecimal> {

	public static BigDecimalEncoder instance = new BigDecimalEncoder();

	protected BigDecimalEncoder() {
		super(BigDecimal.class);
	}

	@Override
	protected void encodeSingleValue(BigDecimal target, Writer writer) throws Exception {
		writer.append(target.toString());
	}
}
