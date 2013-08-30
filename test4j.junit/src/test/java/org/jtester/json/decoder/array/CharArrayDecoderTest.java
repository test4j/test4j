package org.jtester.json.decoder.array;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

public class CharArrayDecoderTest implements JTester {

    @Test
    public void testParseFromJSONArray() {
        String json = "['a',b,\"c\"]";
        Character[] ints = JSON.toObject(json, char[].class);
        want.array(ints).sizeEq(3).reflectionEq(new char[] { 'a', 'b', 'c' });
    }
}
