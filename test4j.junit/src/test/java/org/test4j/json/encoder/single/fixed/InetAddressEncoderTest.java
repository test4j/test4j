package org.test4j.json.encoder.single.fixed;

import java.net.InetAddress;

import org.junit.Test;
import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.single.fixed.InetAddressEncoder;

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
