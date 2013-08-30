package org.jtester.json.decoder.single;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

public class LongDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        Long l = JSON.toObject("1234L", Long.class);
        want.number(l).isEqualTo(1234L);
    }

}
