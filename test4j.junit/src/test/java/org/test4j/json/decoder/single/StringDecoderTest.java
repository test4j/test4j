package org.test4j.json.decoder.single;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class StringDecoderTest implements Test4J {

    @Test
    public void testDecodeSimpleValue() {
        String str = JSON.toObject("test string", String.class);
        want.string(str).isEqualTo("test string");
    }
}
