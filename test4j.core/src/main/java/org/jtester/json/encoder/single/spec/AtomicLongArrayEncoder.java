package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.util.concurrent.atomic.AtomicLongArray;

import org.jtester.json.encoder.single.SpecTypeEncoder;

@SuppressWarnings("rawtypes")
public class AtomicLongArrayEncoder<T extends AtomicLongArray> extends SpecTypeEncoder<T> {

	public static AtomicLongArrayEncoder instance = new AtomicLongArrayEncoder();

	protected AtomicLongArrayEncoder() {
		super(AtomicLongArray.class);
	}

	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void encodeOtherProperty(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub

	}
}
