package org.test4j.json.decoder.single;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class ByteDecoderTest implements Test4J {

    @Test
    public void testDecodeSimpleValue() {
        Byte b = JSON.toObject("1", Byte.class);
        want.bite(b).isEqualTo(Byte.valueOf("1"));
    }
}
