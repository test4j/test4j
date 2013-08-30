package org.jtester.json.encoder.single.fixed;

import java.util.Locale;

import org.jtester.json.encoder.EncoderTest;
import org.testng.annotations.Test;

public class LocaleEncoderTest extends EncoderTest {

	@Test
	public void testLocaleEncoder() {
		Locale locale = Locale.CHINESE;

		LocaleEncoder encoder = LocaleEncoder.instance;
		this.setUnmarkFeature(encoder);
		encoder.encode(locale, writer, references);

		String json = writer.toString();
		want.string(json).isEqualTo("'zh'");
	}
}
