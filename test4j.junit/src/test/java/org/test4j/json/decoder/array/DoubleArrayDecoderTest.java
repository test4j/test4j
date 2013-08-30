package org.test4j.json.decoder.array;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.JTester;

public class DoubleArrayDecoderTest implements JTester {

    @Test
    public void testParseFromJSONArray() {
        String json = "[12,'45.6D',\"78.9d\"]";
        Double[] ints = JSON.toObject(json, double[].class);
        want.array(ints).sizeEq(3).reflectionEq(new double[] { 12d, 45.6d, 78.9d });
    }
}
