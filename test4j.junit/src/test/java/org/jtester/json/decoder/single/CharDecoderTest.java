package org.jtester.json.decoder.single;

import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.json.JSON;

public class CharDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        Character ch = JSON.toObject("'a'", Character.class);
        want.character(ch).isEqualTo('a');
    }
}
