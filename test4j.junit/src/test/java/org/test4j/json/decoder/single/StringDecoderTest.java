package org.test4j.json.decoder.single;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.JTester;

public class StringDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        String str = JSON.toObject("test string", String.class);
        want.string(str).isEqualTo("test string");
    }
}
