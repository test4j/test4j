package org.jtester.json.decoder.single;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

public class SimpleDateFormatDecoderTest implements JTester {
    @Test
    public void testDecode() throws Exception {

        SimpleDateFormat df = JSON.toObject("'yyyy-MM-dd'", SimpleDateFormat.class);

        Date actual = df.parse("2011-09-18");
        want.date(actual).isYear(2011).isMonth(9).isDay(18);
    }
}
