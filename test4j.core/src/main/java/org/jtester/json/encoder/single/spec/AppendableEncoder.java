package org.jtester.json.encoder.single.spec;

import java.io.Writer;

import org.jtester.json.encoder.single.SpecTypeEncoder;
import org.jtester.json.encoder.single.fixed.StringEncoder;

@SuppressWarnings("rawtypes")
public class AppendableEncoder<T extends Appendable> extends SpecTypeEncoder<T> {
	public static AppendableEncoder instance = new AppendableEncoder();

	private AppendableEncoder() {
		super(Appendable.class);
	}

	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		String value = target.toString();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(value, writer);
		writer.append(quote_Char);
	}

	@Override
	protected void encodeOtherProperty(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub

	}
}
