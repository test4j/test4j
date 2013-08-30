package org.jtester.json.encoder.object;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.encoder.object.spec.MethodEncoder;

@SuppressWarnings("rawtypes")
public abstract class SpecEncoder<T> extends JSONEncoder<T> {

	public SpecEncoder(Class clazz) {
		super(clazz);
	}

	@Override
	public boolean encode(T target, Writer writer, List<String> references) {
		try {
			boolean isNullOrRef = this.writerNullOrReference(target, writer, references, true);
			if (isNullOrRef) {
				return false;
			} else {
				this.encodeSpec(target, writer, references);
				return true;
			}
		} catch (Exception e) {
			throw this.wrapException(e);
		}
	}

	protected abstract void encodeSpec(T target, Writer writer, List<String> references) throws Exception;

	public static SpecEncoder isSpecPoJoEncoder(Class type) {
		if (type == Method.class) {
			return MethodEncoder.instance;
		}
		SpecEncoder encoder = SPEC_ENCODERS.get(type);
		return encoder;
	}

	private final static Map<Class, SpecEncoder> SPEC_ENCODERS = new HashMap<Class, SpecEncoder>();

	/**
	 * 添加特殊类型的json编码器
	 * 
	 * @param type
	 * @param encoder
	 */
	public static void addSpecEncoder(Class type, SpecEncoder encoder) {
		SPEC_ENCODERS.put(type, encoder);
	}
}
