package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.util.Locale;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class LocaleEncoder extends FixedTypeEncoder<Locale> {

	public static LocaleEncoder instance = new LocaleEncoder();

	private LocaleEncoder() {
		super(Locale.class);
	}

	@Override
	protected void encodeSingleValue(Locale target, Writer writer) throws Exception {
		String locale = target.toString();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(locale, writer);
		writer.append(quote_Char);
	}
}
