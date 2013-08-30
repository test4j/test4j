package org.test4j.json.decoder.array;

import org.test4j.json.JSON;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

public class CharArrayDecoderTest extends Test4J {

    @Test(groups = { "test4j", "json" })
    public void testParseFromJSONArray() {
        String json = "['a',b,\"c\"]";
        Character[] ints = JSON.toObject(json, char[].class);
        want.array(ints).sizeEq(3).reflectionEq(new char[] { 'a', 'b', 'c' });
    }
}
