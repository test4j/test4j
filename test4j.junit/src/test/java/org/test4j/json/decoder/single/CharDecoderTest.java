package org.test4j.json.decoder.single;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class CharDecoderTest implements Test4J {

    @Test
    public void testDecodeSimpleValue() {
        Character ch = JSON.toObject("'a'", Character.class);
        want.character(ch).isEqualTo('a');
    }
}
