package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.jtester.json.encoder.single.SpecTypeEncoder;

@SuppressWarnings("rawtypes")
public class AtomicReferenceArrayEncoder<T extends AtomicReferenceArray> extends SpecTypeEncoder<T> {
	public static AtomicReferenceArrayEncoder instance = new AtomicReferenceArrayEncoder();

	protected AtomicReferenceArrayEncoder() {
		super(AtomicReferenceArray.class);
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
