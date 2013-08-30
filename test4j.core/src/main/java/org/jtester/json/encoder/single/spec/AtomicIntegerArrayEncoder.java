package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.util.concurrent.atomic.AtomicIntegerArray;

import org.jtester.json.encoder.single.SpecTypeEncoder;

@SuppressWarnings("rawtypes")
public class AtomicIntegerArrayEncoder<T extends AtomicIntegerArray> extends SpecTypeEncoder<T> {

	public static AtomicIntegerArrayEncoder instance = new AtomicIntegerArrayEncoder();

	protected AtomicIntegerArrayEncoder() {
		super(AtomicIntegerArray.class);
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
