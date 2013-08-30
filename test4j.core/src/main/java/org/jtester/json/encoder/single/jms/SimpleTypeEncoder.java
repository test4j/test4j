package org.jtester.json.encoder.single.jms;

import java.io.Writer;

import javax.management.openmbean.SimpleType;

import org.jtester.json.encoder.single.FixedTypeEncoder;
import org.jtester.json.encoder.single.fixed.StringEncoder;

@SuppressWarnings("rawtypes")
public class SimpleTypeEncoder extends FixedTypeEncoder<SimpleType> {

	protected SimpleTypeEncoder() {
		super(SimpleType.class);
	}

	@Override
	protected void encodeSingleValue(SimpleType target, Writer writer) throws Exception {
		String typename = target.getTypeName();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(typename, writer);
		writer.append(quote_Char);
	}
}
