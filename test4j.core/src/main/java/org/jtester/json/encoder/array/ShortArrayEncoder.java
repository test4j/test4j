package org.jtester.json.encoder.array;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.encoder.single.fixed.ShortEncoder;

@SuppressWarnings("rawtypes")
public class ShortArrayEncoder extends ArraysEncoder<short[]> {
	public final static ShortArrayEncoder instance = new ShortArrayEncoder();

	private ShortArrayEncoder() {
		super(short.class);
	}

	@Override
	protected int getArraySize(short[] target) {
		return target.length;
	}

	@Override
	protected JSONEncoder getEncoderByItem(Object item) {
		return ShortEncoder.instance;
	}

	@Override
	protected Object getItemByIndex(short[] target, int index) {
		return target[index];
	}
}
