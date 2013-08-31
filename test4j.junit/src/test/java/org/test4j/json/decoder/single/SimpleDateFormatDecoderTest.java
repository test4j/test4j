package org.test4j.json.decoder.single;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class SimpleDateFormatDecoderTest extends Test4J {
    @Test
    public void testDecode() throws Exception {

        SimpleDateFormat df = JSON.toObject("'yyyy-MM-dd'", SimpleDateFormat.class);

        Date actual = df.parse("2011-09-18");
        want.date(actual).isYear(2011).isMonth(9).isDay(18);
    }
}
