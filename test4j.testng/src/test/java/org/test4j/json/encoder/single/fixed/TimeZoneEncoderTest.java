package org.test4j.json.encoder.single.fixed;

import java.util.TimeZone;

import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.single.fixed.TimeZoneEncoder;
import org.testng.annotations.Test;

public class TimeZoneEncoderTest extends EncoderTest {

	@Test
	public void testTimeZoneEncoder() {
		TimeZone zone = TimeZone.getTimeZone("GMT-8:00");
		TimeZoneEncoder encoder = TimeZoneEncoder.instance;
		this.setUnmarkFeature(encoder);

		encoder.encode(zone, writer, references);
		String json = writer.toString();
		want.string(json).isEqualTo("'GMT-08:00'");
	}
}
