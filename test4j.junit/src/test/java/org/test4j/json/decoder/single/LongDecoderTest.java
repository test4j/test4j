package org.test4j.json.decoder.single;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class LongDecoderTest extends Test4J {

    @Test
    public void testDecodeSimpleValue() {
        Long l = JSON.toObject("1234L", Long.class);
        want.number(l).isEqualTo(1234L);
    }

}
