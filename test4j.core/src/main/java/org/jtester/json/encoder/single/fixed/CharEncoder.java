package org.jtester.json.encoder.single.fixed;

import java.io.Writer;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class CharEncoder extends FixedTypeEncoder<Character> {
	public static CharEncoder instance = new CharEncoder();

	private CharEncoder() {
		super(Character.class);
	}

	@Override
	protected void encodeSingleValue(Character target, Writer writer) throws Exception {
		writer.append(quote_Char);
		StringEncoder.writerChar(target, writer);
		writer.append(quote_Char);
	}
}
