package org.jtester.json.encoder.array;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.encoder.single.fixed.LongEncoder;

@SuppressWarnings("rawtypes")
public class LongArrayEncoder extends ArraysEncoder<long[]> {
	public final static LongArrayEncoder instance = new LongArrayEncoder();

	private LongArrayEncoder() {
		super(long.class);
	}

	@Override
	protected int getArraySize(long[] target) {
		return target.length;
	}

	@Override
	protected JSONEncoder getEncoderByItem(Object item) {
		return LongEncoder.instance;
	}

	@Override
	protected Object getItemByIndex(long[] target, int index) {
		return target[index];
	}
}
