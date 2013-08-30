package org.test4j.json.decoder.single;

import org.test4j.json.JSON;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "json" })
public class StringDecoderTest extends Test4J {

    @Test
    public void testDecodeSimpleValue() {
        String str = JSON.toObject("test string", String.class);
        want.string(str).isEqualTo("test string");
    }
}
