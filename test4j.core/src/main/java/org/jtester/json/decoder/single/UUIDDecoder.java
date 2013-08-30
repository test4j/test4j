package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.util.UUID;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class UUIDDecoder extends FixedTypeDecoder {
	public final static UUIDDecoder toUUID = new UUIDDecoder();

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return UUID.class.isAssignableFrom(claz);
	}

	@Override
	protected UUID decodeFromString(String value) {
		UUID uuid = UUID.fromString(value);
		return uuid;
	}
}
