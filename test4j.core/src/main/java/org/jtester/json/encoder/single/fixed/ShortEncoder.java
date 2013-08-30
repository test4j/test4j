package org.jtester.json.encoder.single.fixed;

import java.io.Writer;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class ShortEncoder extends FixedTypeEncoder<Short> {
	public static ShortEncoder instance = new ShortEncoder();

	private ShortEncoder() {
		super(Short.class);
	}

	@Override
	protected void encodeSingleValue(Short target, Writer writer) throws Exception {
		writer.append(String.valueOf(target));
	}
}
