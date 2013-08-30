package org.jtester.json.encoder.single.fixed;

import java.io.Writer;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class DoubleEncoder extends FixedTypeEncoder<Double> {
	public static DoubleEncoder instance = new DoubleEncoder();

	private DoubleEncoder() {
		super(Double.class);
	}

	@Override
	protected void encodeSingleValue(Double target, Writer writer) throws Exception {
		String text = Double.toString(target);
		if (text.endsWith(".0")) {
			text = text.substring(0, text.length() - 2);
		}
		writer.append(text);
	}

}
