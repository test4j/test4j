package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.util.regex.Pattern;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class PatternEncoder extends FixedTypeEncoder<Pattern> {
	public static PatternEncoder instance = new PatternEncoder();

	private PatternEncoder() {
		super(Pattern.class);
	}

	@Override
	protected void encodeSingleValue(Pattern target, Writer writer) throws Exception {
		String pattern = target.pattern();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(pattern, writer);
		writer.append(quote_Char);
	}
}
