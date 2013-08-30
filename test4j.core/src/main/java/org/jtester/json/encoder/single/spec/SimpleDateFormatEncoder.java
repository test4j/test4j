package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.text.SimpleDateFormat;

import org.jtester.json.encoder.single.SpecTypeEncoder;
import org.jtester.json.encoder.single.fixed.StringEncoder;

@SuppressWarnings("rawtypes")
public class SimpleDateFormatEncoder<T extends SimpleDateFormat> extends SpecTypeEncoder<T> {

	public static SimpleDateFormatEncoder instance = new SimpleDateFormatEncoder();

	protected SimpleDateFormatEncoder() {
		super(SimpleDateFormat.class);
	}

	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		String pattern = target.toPattern();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(pattern, writer);
		writer.append(quote_Char);
	}

	@Override
	protected void encodeOtherProperty(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub
	}
}
