package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.util.concurrent.atomic.AtomicLong;

import org.jtester.json.encoder.single.SpecTypeEncoder;

@SuppressWarnings("rawtypes")
public class AtomicLongEncoder<T extends AtomicLong> extends SpecTypeEncoder<T> {

	public static AtomicLongEncoder instance = new AtomicLongEncoder();

	protected AtomicLongEncoder() {
		super(AtomicLong.class);
	}

	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		long value = target.get();
		writer.append(String.valueOf(value));
	}

	@Override
	protected void encodeOtherProperty(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub

	}
}
