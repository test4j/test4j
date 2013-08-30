package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;

import org.jtester.json.encoder.single.SpecTypeEncoder;

@SuppressWarnings("rawtypes")
public class AtomicIntegerEncoder<T extends AtomicInteger> extends SpecTypeEncoder<T> {

	public static AtomicIntegerEncoder instance = new AtomicIntegerEncoder();

	private AtomicIntegerEncoder() {
		super(AtomicInteger.class);
	}

	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		int value = target.get();
		writer.append(String.valueOf(value));
	}

	@Override
	protected void encodeOtherProperty(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub

	}

}
