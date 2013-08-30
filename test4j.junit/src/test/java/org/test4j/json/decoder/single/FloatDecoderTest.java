package org.test4j.json.decoder.single;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.JTester;

public class FloatDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        Float f = JSON.toObject("12.34f", Float.class);
        want.number(f).isEqualTo(12.34f);
    }
}
