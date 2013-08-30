package org.test4j.json.encoder.single.fixed;

import java.io.Writer;

import org.test4j.json.encoder.single.FixedTypeEncoder;

public class LongEncoder extends FixedTypeEncoder<Long> {
	public static LongEncoder instance = new LongEncoder();

	private LongEncoder() {
		super(Long.class);
	}

	@Override
	protected void encodeSingleValue(Long target, Writer writer) throws Exception {
		writer.append(String.valueOf(target));
	}
}
