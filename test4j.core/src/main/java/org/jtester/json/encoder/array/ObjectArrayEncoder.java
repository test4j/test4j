package org.jtester.json.encoder.array;

import java.util.HashMap;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.encoder.object.PoJoEncoder;

@SuppressWarnings({ "rawtypes" })
public class ObjectArrayEncoder<T> extends ArraysEncoder<T[]> {

	public ObjectArrayEncoder(Class clazz) {
		super(clazz);
		if (this.clazz == Object.class) {
			this.clazz = HashMap.class;
		}
	}

	@Override
	protected int getArraySize(T[] target) {
		return target.length;
	}

	@Override
	protected JSONEncoder getEncoderByItem(Object item) {
		if (item == null) {
			return new PoJoEncoder(Object.class);
		}
		Class type = item.getClass();
		return JSONEncoder.get(type);
	}

	@Override
	protected Object getItemByIndex(T[] target, int index) {
		return target[index];
	}
}
