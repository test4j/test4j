package org.jtester.json.decoder.array;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

public class LongArrayDecoderTest implements JTester {

    @Test
    public void testParseFromJSONArray() {
        String json = "['12',124L,\"456l\"]";
        Long[] ints = JSON.toObject(json, long[].class);
        want.array(ints).sizeEq(3).reflectionEq(new long[] { 12L, 124L, 456L });
    }
}
