package org.jtester.json.encoder.single;

import java.io.Writer;
import java.util.List;

import org.jtester.json.encoder.SingleEncoder;
import org.jtester.json.helper.JSONFeature;

@SuppressWarnings({ "rawtypes" })
public abstract class FixedTypeEncoder<T> extends SingleEncoder<T> {
	protected FixedTypeEncoder(Class clazz) {
		super(clazz);
	}

	@Override
	public final boolean encode(T target, Writer writer, List<String> references) {
		try {
			return this.encodeFinalType(target, writer);
		} catch (Exception e) {
			throw this.wrapException(e);
		}
	}

	private final boolean encodeFinalType(T target, Writer writer) throws Exception {
		if (target == null) {
			writer.append("null");
			return true;
		}

		if (this.unMarkClassFlag) {
			this.encodeSingleValue(target, writer);
		} else {
			writer.append("{");
			this.writeClassFlag(target, writer);
			writer.append(',');
			this.writerSpecProperty(JSONFeature.ValueFlag, writer);
			writer.append(':');
			this.encodeSingleValue(target, writer);
			writer.append('}');
		}
		return true;
	}

	protected abstract void encodeSingleValue(T target, Writer writer) throws Exception;
}
