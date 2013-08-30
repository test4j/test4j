package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.net.URI;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class URIDecoder extends FixedTypeDecoder {
	public final static URIDecoder toURI = new URIDecoder();

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return URI.class.isAssignableFrom(claz);
	}

	@Override
	protected URI decodeFromString(String value) {
		URI uri = URI.create(value);
		return uri;
	}
}
