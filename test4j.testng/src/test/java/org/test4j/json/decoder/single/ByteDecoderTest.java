package org.test4j.json.decoder.single;

import org.test4j.json.JSON;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "json" })
public class ByteDecoderTest extends Test4J {

    @Test
    public void testDecodeSimpleValue() {
        Byte b = JSON.toObject("1", Byte.class);
        want.bite(b).isEqualTo(Byte.valueOf("1"));
    }
}
