package org.jtester.json.decoder.single;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class SimpleDateFormatDecoderTest extends JTester {

	public void testDecode() throws Exception {

		SimpleDateFormat df = JSON.toObject("'yyyy-MM-dd'", SimpleDateFormat.class);

		Date actual = df.parse("2011-09-18");
		want.date(actual).isYear(2011).isMonth(9).isDay(18);
	}
}
