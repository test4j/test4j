package org.test4j.json.decoder.array;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class CharArrayDecoderTest extends Test4J {

    @Test
    public void testParseFromJSONArray() {
        String json = "['a',b,\"c\"]";
        Character[] ints = JSON.toObject(json, char[].class);
        want.array(ints).sizeEq(3).reflectionEq(new char[] { 'a', 'b', 'c' });
    }
}
