package org.jtester.json.encoder.single.fixed;

import java.io.Writer;

import org.jtester.json.encoder.single.FixedTypeEncoder;

@SuppressWarnings("rawtypes")
public class EnumEncoder<T extends Enum> extends FixedTypeEncoder<T> {
	public static EnumEncoder instance = new EnumEncoder();

	public EnumEncoder() {
		super(Enum.class);
	}

	@Override
	protected void encodeSingleValue(Enum target, Writer writer) throws Exception {
		String name = target.name();
		writer.append(name);
	}
}
