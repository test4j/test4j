package org.jtester.json.encoder.single.fixed;

import java.io.Writer;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class BooleanEncoder extends FixedTypeEncoder<Boolean> {
	public static BooleanEncoder instance = new BooleanEncoder();

	private BooleanEncoder() {
		super(Boolean.class);
	}

	@Override
	protected void encodeSingleValue(Boolean target, Writer writer) throws Exception {
		writer.append(target ? "true" : "false");
	}
}
