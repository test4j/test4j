package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class SocketAddressEncoder extends FixedTypeEncoder<InetSocketAddress> {

	public static SocketAddressEncoder instance = new SocketAddressEncoder();

	private SocketAddressEncoder() {
		super(InetSocketAddress.class);
	}

	@Override
	protected void encodeSingleValue(InetSocketAddress target, Writer writer) throws Exception {
		InetAddress address = target.getAddress();
		int port = target.getPort();

		String host = address == null ? "localhost" : address.getHostAddress();

		writer.append(quote_Char);
		StringEncoder.writeEscapeString(host, writer);
		writer.append(':');
		writer.append(String.valueOf(port));
		writer.append(quote_Char);
	}
}
