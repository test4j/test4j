package org.jtester.json.decoder.array;

import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.json.JSON;

public class IntArrayDecoderTest implements JTester {

    @Test
    public void testParseFromJSONArray() {
        String json = "['1','2','3']";
        Integer[] ints = JSON.toObject(json, int[].class);
        want.array(ints).sizeEq(3).reflectionEq(new int[] { 1, 2, 3 });
    }
}
