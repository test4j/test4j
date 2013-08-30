package org.jtester.json.encoder.array;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.encoder.single.fixed.FloatEncoder;

@SuppressWarnings("rawtypes")
public class FloatArrayEncoder extends ArraysEncoder<float[]> {
	public final static FloatArrayEncoder instance = new FloatArrayEncoder();

	private FloatArrayEncoder() {
		super(float.class);
	}

	@Override
	protected int getArraySize(float[] target) {
		return target.length;
	}

	@Override
	protected JSONEncoder getEncoderByItem(Object item) {
		return FloatEncoder.instance;
	}

	@Override
	protected Object getItemByIndex(float[] target, int index) {
		return target[index];
	}
}
