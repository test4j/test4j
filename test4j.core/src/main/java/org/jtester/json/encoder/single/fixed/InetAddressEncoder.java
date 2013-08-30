package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.net.InetAddress;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class InetAddressEncoder extends FixedTypeEncoder<InetAddress> {
	public static InetAddressEncoder instance = new InetAddressEncoder();

	private InetAddressEncoder() {
		super(InetAddress.class);
	}

	@Override
	protected void encodeSingleValue(InetAddress target, Writer writer) throws Exception {
		String host = target.getHostAddress();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(host, writer);
		writer.append(quote_Char);
	}
}
