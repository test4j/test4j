package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class URLDecoder extends FixedTypeDecoder {
	public final static URLDecoder toURL = new URLDecoder();

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return URL.class.isAssignableFrom(claz);
	}

	@Override
	protected URL decodeFromString(String value) {
		try {
			URL url = URI.create(value).toURL();
			return url;
		} catch (MalformedURLException e) {
			String message = "the value " + value + " can't cast to URL.";
			throw new DecoderException(message, e);
		}
	}
}
