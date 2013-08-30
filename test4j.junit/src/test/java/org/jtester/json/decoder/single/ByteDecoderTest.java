package org.jtester.json.decoder.single;

import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.json.JSON;

public class ByteDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        Byte b = JSON.toObject("1", Byte.class);
        want.bite(b).isEqualTo(Byte.valueOf("1"));
    }
}
