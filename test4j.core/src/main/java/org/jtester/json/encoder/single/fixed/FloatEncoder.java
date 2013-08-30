package org.jtester.json.encoder.single.fixed;

import java.io.Writer;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class FloatEncoder extends FixedTypeEncoder<Float> {
	public static FloatEncoder instance = new FloatEncoder();

	private FloatEncoder() {
		super(Float.class);
	}

	@Override
	protected void encodeSingleValue(Float target, Writer writer) throws Exception {
		String text = Float.toString(target);
		if (text.endsWith(".0")) {
			text = text.substring(0, text.length() - 2);
		}
		writer.append(text);
	}
}
