package org.jtester.json.encoder.array;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.encoder.single.fixed.CharEncoder;

@SuppressWarnings("rawtypes")
public class CharArrayEncoder extends ArraysEncoder<char[]> {
	public final static CharArrayEncoder instance = new CharArrayEncoder();

	private CharArrayEncoder() {
		super(char.class);
	}

	@Override
	protected int getArraySize(char[] target) {
		return target.length;
	}

	@Override
	protected JSONEncoder getEncoderByItem(Object item) {
		return CharEncoder.instance;
	}

	@Override
	protected Object getItemByIndex(char[] target, int index) {
		return target[index];
	}
}
