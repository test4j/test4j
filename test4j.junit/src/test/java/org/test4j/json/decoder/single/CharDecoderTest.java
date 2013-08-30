package org.test4j.json.decoder.single;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.JTester;

public class CharDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        Character ch = JSON.toObject("'a'", Character.class);
        want.character(ch).isEqualTo('a');
    }
}
