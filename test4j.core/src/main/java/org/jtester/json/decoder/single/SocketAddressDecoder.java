package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SocketAddressDecoder extends FixedTypeDecoder {

	public final static SocketAddressDecoder toSOCKETADDRESS = new SocketAddressDecoder();

	@Override
	protected InetSocketAddress decodeFromString(String value) {
		String[] parts = value.split(":");
		if (parts.length == 1) {
			return new InetSocketAddress(parts[0], 0);
		}
		int port = Integer.valueOf(parts[1]);
		return new InetSocketAddress(parts[0], port);
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return InetSocketAddress.class.isAssignableFrom(claz);
	}
}
