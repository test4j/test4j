package org.jtester.json.encoder.array;

import java.io.Writer;
import java.util.List;

import org.jtester.json.encoder.ArrayEncoder;
import org.jtester.json.encoder.JSONEncoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class ArraysEncoder<T> extends ArrayEncoder<T> {

	public ArraysEncoder(Class clazz) {
		super(clazz);
	}

	@Override
	protected void encodeIterator(T target, Writer writer, List<String> references) throws Exception {
		boolean isFirst = true;
		int size = this.getArraySize(target);
		for (int index = 0; index < size; index++) {
			if (isFirst) {
				isFirst = false;
			} else {
				writer.append(',');
			}

			Object item = this.getItemByIndex(target, index);
			boolean isNullOrRef = this.writerNullOrReference(item, writer, references, false);
			if (isNullOrRef == false) {
				JSONEncoder baseEncoder = this.getEncoderByItem(item);
				baseEncoder.setFeatures(this.features);
				baseEncoder.encode(item, writer, references);
			}
		}
	}

	protected abstract int getArraySize(T target);

	protected abstract JSONEncoder getEncoderByItem(Object item);

	protected abstract Object getItemByIndex(T target, int index);

	public static ArraysEncoder newInstance(Class type) {
		if (type == boolean[].class) {
			return BooleanArrayEncoder.instance;
		}
		if (type == byte[].class) {
			return ByteArrayEncoder.instance;
		}
		if (type == char[].class) {
			return CharArrayEncoder.instance;
		}
		if (type == int[].class) {
			return IntegerArrayEncoder.instance;
		}
		if (type == long[].class) {
			return LongArrayEncoder.instance;
		}
		if (type == short[].class) {
			return ShortArrayEncoder.instance;
		}
		if (type == double[].class) {
			return DoubleArrayEncoder.instance;
		}
		if (type == float[].class) {
			return FloatArrayEncoder.instance;
		}

		return new ObjectArrayEncoder(type);
	}
}
