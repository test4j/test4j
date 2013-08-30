package org.jtester.json.decoder.single;

import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.json.JSON;

public class FloatDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        Float f = JSON.toObject("12.34f", Float.class);
        want.number(f).isEqualTo(12.34f);
    }
}
