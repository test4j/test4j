package org.jtester.json.encoder.single;

import java.io.Writer;
import java.util.List;

import org.jtester.json.encoder.SingleEncoder;
import org.jtester.json.helper.JSONFeature;

@SuppressWarnings({ "rawtypes" })
public abstract class SpecTypeEncoder<T> extends SingleEncoder<T> {

	protected SpecTypeEncoder(Class clazz) {
		super(clazz);
	}

	@Override
	public final boolean encode(T target, Writer writer, List<String> references) {
		try {
			return this.encodeSpecType(target, writer, references);
		} catch (Exception e) {
			throw this.wrapException(e);
		}
	}

	private final boolean encodeSpecType(T target, Writer writer, List<String> references) throws Exception {
		boolean isNullOrRef = this.writerNullOrReference(target, writer, references, true);
		if (isNullOrRef) {
			return false;
		}

		Class type = target.getClass();
		if (type == this.clazz) {
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
		} else {
			writer.append("{");
			if (this.unMarkClassFlag == false) {
				this.writeClassFlag(target, writer);
				writer.append(',');
			}
			this.writerSpecProperty(JSONFeature.ValueFlag, writer);
			writer.append(':');
			this.encodeSingleValue(target, writer);

			this.encodeOtherProperty(target, writer);
			writer.append('}');
		}
		return true;
	}

	protected abstract void encodeSingleValue(T target, Writer writer) throws Exception;

	protected abstract void encodeOtherProperty(T target, Writer writer) throws Exception;
}
