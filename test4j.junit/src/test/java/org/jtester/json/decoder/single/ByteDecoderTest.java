package org.jtester.json.decoder.single;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

public class ByteDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        Byte b = JSON.toObject("1", Byte.class);
        want.bite(b).isEqualTo(Byte.valueOf("1"));
    }
}
