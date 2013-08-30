package org.jtester.json.decoder.single;

import java.lang.reflect.Type;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BooleanDecoder extends FixedTypeDecoder {
	public final static BooleanDecoder toBOOLEAN = new BooleanDecoder();

	@Override
	protected Boolean decodeFromString(String value) {
		value = value.trim();
		return toBoolean(value);
	}

	/**
	 * 将字符串转为布尔值
	 * 
	 * @param value
	 * @return
	 */
	public static boolean toBoolean(String value) {
		if ("true".equalsIgnoreCase(value) || "1".equals(value)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Boolean.class == claz || boolean.class == claz;
	}
}
