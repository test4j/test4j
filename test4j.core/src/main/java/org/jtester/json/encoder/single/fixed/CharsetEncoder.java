package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.nio.charset.Charset;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class CharsetEncoder extends FixedTypeEncoder<Charset> {

	public static CharsetEncoder instance = new CharsetEncoder();

	protected CharsetEncoder() {
		super(Charset.class);
	}

	@Override
	protected void encodeSingleValue(Charset target, Writer writer) throws Exception {
		writer.append(quote_Char).append(target.name()).append(quote_Char);
	}
}
