package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class InetAddressDecoder extends FixedTypeDecoder {
	public static InetAddressDecoder toINETADDRESS = new InetAddressDecoder();

	@Override
	protected InetAddress decodeFromString(String host) {
		try {
			return InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			String message = "can't cast value[" + host + "] to InetAddress.";
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return InetAddress.class.isAssignableFrom(claz);
	}
}
