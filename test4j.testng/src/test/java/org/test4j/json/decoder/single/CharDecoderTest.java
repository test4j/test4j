package org.test4j.json.decoder.single;

import org.test4j.json.JSON;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "json" })
public class CharDecoderTest extends Test4J {

    @Test
    public void testDecodeSimpleValue() {
        Character ch = JSON.toObject("'a'", Character.class);
        want.character(ch).isEqualTo('a');
    }
}
