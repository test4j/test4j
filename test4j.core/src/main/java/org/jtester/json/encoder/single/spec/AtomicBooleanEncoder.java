package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jtester.json.encoder.single.SpecTypeEncoder;

@SuppressWarnings("rawtypes")
public class AtomicBooleanEncoder<T extends AtomicBoolean> extends SpecTypeEncoder<T> {

	public static AtomicBooleanEncoder instance = new AtomicBooleanEncoder();

	private AtomicBooleanEncoder() {
		super(AtomicBoolean.class);
	}

	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		boolean value = target.get();
		writer.append(value ? "true" : "false");
	}

	@Override
	protected void encodeOtherProperty(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub

	}
}
