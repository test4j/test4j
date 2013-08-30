package org.test4j.json.decoder.single;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.JTester;

public class ByteDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        Byte b = JSON.toObject("1", Byte.class);
        want.bite(b).isEqualTo(Byte.valueOf("1"));
    }
}
