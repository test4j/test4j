package org.jtester.json.encoder.array;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.encoder.single.fixed.BooleanEncoder;

@SuppressWarnings("rawtypes")
public class BooleanArrayEncoder extends ArraysEncoder<boolean[]> {
	public final static BooleanArrayEncoder instance = new BooleanArrayEncoder();

	private BooleanArrayEncoder() {
		super(boolean.class);
	}

	@Override
	protected int getArraySize(boolean[] target) {
		return target.length;
	}

	@Override
	protected JSONEncoder getEncoderByItem(Object item) {
		return BooleanEncoder.instance;
	}

	@Override
	protected Object getItemByIndex(boolean[] target, int index) {
		return target[index];
	}
}
