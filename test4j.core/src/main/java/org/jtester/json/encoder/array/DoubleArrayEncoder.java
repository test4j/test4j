package org.jtester.json.encoder.array;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.encoder.single.fixed.DoubleEncoder;

@SuppressWarnings("rawtypes")
public class DoubleArrayEncoder extends ArraysEncoder<double[]> {
	public final static DoubleArrayEncoder instance = new DoubleArrayEncoder();

	private DoubleArrayEncoder() {
		super(double.class);
	}

	@Override
	protected int getArraySize(double[] target) {
		return target.length;
	}

	@Override
	protected JSONEncoder getEncoderByItem(Object item) {
		return DoubleEncoder.instance;
	}

	@Override
	protected Object getItemByIndex(double[] target, int index) {
		return target[index];
	}
}
