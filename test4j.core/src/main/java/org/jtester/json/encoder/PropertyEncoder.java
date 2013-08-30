package org.jtester.json.encoder;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

import org.jtester.json.JSONException;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.FieldHelper;

public abstract class PropertyEncoder extends JSONEncoder<Object> {
	protected PropertyEncoder(int features) {
		super(Object.class);
		this.setFeatures(features);
	}

	/**
	 * 编码对象字段名称或者 Key-Value 的key值
	 * 
	 * @param owner
	 *            字段的所有者，或者K-V所有者Map
	 * @param writer
	 * @param references
	 * @throws Exception
	 */
	protected abstract void encodeKey(Writer writer, List<String> references) throws Exception;

	/**
	 * 编码对象字段值或者 Key-Value 的value值
	 * 
	 * @param owner
	 *            字段的所有者，或者K-V所有者Map
	 * @param writer
	 * @param references
	 * @throws Exception
	 */
	protected abstract void encodeValue(Writer writer, List<String> references) throws Exception;

	/**
	 * {@inheritDoc}<br>
	 * <br>
	 * target 是属性的owner对象
	 * 
	 */
	@Override
	public final boolean encode(Object target, Writer writer, List<String> references) {
		if (this.doesSkipNull() == true) {
			return false;
		}
		try {
			this.encodeKey(writer, references);
			writer.write(":");
			this.encodeValue(writer, references);
			return true;
		} catch (Exception e) {
			throw this.wrapException(e);
		}
	}

	public static PropertyEncoder newInstance(Field field, Object target, int features) {
		return new FieldEncoder(field, target, features);
	}

	public static PropertyEncoder newInstance(Object key, Object value, int features) {
		return new MapEntryEncoder(key, value, features);
	}

	public abstract boolean doesSkipNull();
}

/**
 * K-V 对象的编码器
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
class MapEntryEncoder extends PropertyEncoder {
	private final Object key;
	private final Object value;

	public MapEntryEncoder(Object key, Object value, int features) {
		super(features);
		this.key = key;
		this.value = value;
	}

	@Override
	public void encodeKey(Writer writer, List<String> references) throws Exception {
		boolean isRef = this.writerNullOrReference(this.key, writer, references, false);
		if (isRef) {
			return;
		}
		Class type = key.getClass();
		JSONEncoder encoder = JSONEncoder.get(type);
		encoder.setFeatures(features);

		try {
			encoder.encode(this.key, writer, references);
		} catch (Exception e) {
			throw new JSONException("encode map key error.", e);
		}
	}

	@Override
	public void encodeValue(Writer writer, List<String> references) throws IOException {
		boolean isNullOrRef = this.writerNullOrReference(this.value, writer, references, false);
		if (isNullOrRef) {
			return;
		}
		Class type = this.value.getClass();
		JSONEncoder encoder = JSONEncoder.get(type);
		encoder.setFeatures(features);
		try {
			encoder.encode(this.value, writer, references);
		} catch (Exception e) {
			throw new JSONException("encode map value error.", e);
		}
	}

	@Override
	public boolean doesSkipNull() {
		return false;
	}
}

/**
 * pojo对象的字段编码器
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
class FieldEncoder extends PropertyEncoder {
	private final Field field;

	private final String fieldName;

	private final Object value;

	public FieldEncoder(Field field, Object target, int features) {
		super(features);
		this.field = field;
		this.fieldName = this.field.getName();
		this.value = FieldHelper.getFieldValue(target, field);
	}

	public void encodeKey(Writer writer, List<String> references) throws Exception {
		this.writerSpecProperty(this.fieldName, writer);
	}

	public void encodeValue(Writer writer, List<String> references) throws IOException {
		boolean isNullOrRef = this.writerNullOrReference(value, writer, references, false);
		if (isNullOrRef) {
			return;
		}
		Class objType = ClazzHelper.getUnProxyType(value.getClass());

		JSONEncoder encoder = JSONEncoder.get(objType);
		encoder.setFeatures(features);
		if (this.doesIgnoreExplicitFieldType(field.getType(), objType)) {
			encoder.setUnMarkClassFlag(true);
		}
		try {
			encoder.encode(value, writer, references);
		} catch (Exception e) {
			throw new JSONException("encode field[" + fieldName + "] error.", e);
		}
	}

	private boolean doesIgnoreExplicitFieldType(Class fieldType, Class objectType) {
		if (this.ignoreExplicitFieldType == false) {
			return false;
		}
		if (field.getType() == objectType) {
			return true;
		}
		if (fieldType.isPrimitive()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean doesSkipNull() {
		return this.skipNullField && this.value == null;
	}
}
