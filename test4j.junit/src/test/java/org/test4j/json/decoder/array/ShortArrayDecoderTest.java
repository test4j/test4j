package org.test4j.json.decoder.array;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class ShortArrayDecoderTest implements Test4J {

    @Test
    public void testParseFromJSONArray() {
        String json = "['12',124,\"456\"]";
        Short[] ints = JSON.toObject(json, short[].class);
        want.array(ints).sizeEq(3).reflectionEq(new short[] { 12, 124, 456 });
    }
}
