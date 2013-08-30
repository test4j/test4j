package org.jtester.json.encoder.single.fixed;

import java.io.Writer;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class IntegerEncoder extends FixedTypeEncoder<Integer> {
	public static IntegerEncoder instance = new IntegerEncoder();

	private IntegerEncoder() {
		super(Integer.class);
	}

	@Override
	protected void encodeSingleValue(Integer target, Writer writer) throws Exception {
		writer.append(String.valueOf(target));
	}
}
