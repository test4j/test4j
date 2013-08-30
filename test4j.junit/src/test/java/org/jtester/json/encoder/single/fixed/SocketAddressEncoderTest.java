package org.jtester.json.encoder.single.fixed;

import java.net.InetSocketAddress;

import org.jtester.json.encoder.EncoderTest;
import org.junit.Test;

public class SocketAddressEncoderTest extends EncoderTest {

	@Test
	public void testSocketAddressEncoder() {
		InetSocketAddress socket = new InetSocketAddress("localhost", 9090);

		SocketAddressEncoder encoder = SocketAddressEncoder.instance;
		this.setUnmarkFeature(encoder);
		encoder.encode(socket, writer, references);

		String json = writer.toString();
		want.string(json).isEqualTo("'127.0.0.1:9090'");
	}
}
