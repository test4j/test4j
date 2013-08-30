package org.jtester.json.decoder.array;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

public class DoubleArrayDecoderTest implements JTester {

    @Test
    public void testParseFromJSONArray() {
        String json = "[12,'45.6D',\"78.9d\"]";
        Double[] ints = JSON.toObject(json, double[].class);
        want.array(ints).sizeEq(3).reflectionEq(new double[] { 12d, 45.6d, 78.9d });
    }
}
