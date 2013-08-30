package org.jtester.json.encoder.single.fixed;

import java.net.InetAddress;

import org.jtester.json.encoder.EncoderTest;
import org.junit.Test;

public class InetAddressEncoderTest extends EncoderTest {

	@Test
	public void testInetAddressEncoder() throws Exception {
		InetAddress ad = InetAddress.getByName("127.0.0.1");
		InetAddressEncoder encoder = InetAddressEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(ad, writer, references);
		String json = writer.toString();
		want.string(json).isEqualTo("'127.0.0.1'");
	}
}
