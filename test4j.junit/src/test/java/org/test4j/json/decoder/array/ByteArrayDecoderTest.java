package org.test4j.json.decoder.array;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class ByteArrayDecoderTest extends Test4J {

    @Test
    public void testParseFromJSONArray() {
        String json = "['1','0',1]";
        Byte[] ints = JSON.toObject(json, byte[].class);
        want.array(ints).sizeEq(3).reflectionEq(new byte[] { 1, 0, 1 });
    }
}
