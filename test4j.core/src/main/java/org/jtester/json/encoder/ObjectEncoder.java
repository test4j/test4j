package org.jtester.json.encoder;

import java.io.Writer;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({ "rawtypes" })
public abstract class ObjectEncoder<T> extends JSONEncoder<T> {

	protected ObjectEncoder(Class clazz) {
		super(clazz);
	}

	public boolean encode(T target, Writer writer, List<String> references) {
		try {
			return this.encodeObject(target, writer, references);
		} catch (Exception e) {
			throw this.wrapException(e);
		}
	}

	private final boolean encodeObject(T target, Writer writer, List<String> references) throws Exception {
		boolean isNullOrRef = this.writerNullOrReference(target, writer, references, true);
		if (isNullOrRef) {
			return false;
		}
		Collection<PropertyEncoder> encoders = this.getPropertyEncoders(target);

		writer.write("{");
		boolean comma = this.writeClassFlag(target, writer);
		for (PropertyEncoder encoder : encoders) {
			if (encoder.doesSkipNull()) {
				continue;
			}
			if (comma) {
				writer.write(",");
			} else {
				comma = true;
			}
			comma = encoder.encode(target, writer, references);
		}

		writer.write("}");
		return true;
	}

	/**
	 * 返回对象需要序列化的属性列表
	 * 
	 * @param target
	 * @return
	 */
	protected abstract Collection<PropertyEncoder> getPropertyEncoders(T target);
}
