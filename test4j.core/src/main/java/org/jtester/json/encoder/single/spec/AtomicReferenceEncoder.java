package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.util.concurrent.atomic.AtomicReference;

import org.jtester.json.encoder.single.SpecTypeEncoder;

@SuppressWarnings("rawtypes")
public class AtomicReferenceEncoder<T extends AtomicReference> extends SpecTypeEncoder<T> {
	public static AtomicReferenceEncoder instance = new AtomicReferenceEncoder();

	protected AtomicReferenceEncoder() {
		super(AtomicReference.class);
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
