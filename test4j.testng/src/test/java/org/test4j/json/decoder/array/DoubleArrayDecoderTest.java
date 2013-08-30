package org.test4j.json.decoder.array;

import org.test4j.json.JSON;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

public class DoubleArrayDecoderTest extends Test4J {

    @Test(groups = { "test4j", "json" })
    public void testParseFromJSONArray() {
        String json = "[12,'45.6D',\"78.9d\"]";
        Double[] ints = JSON.toObject(json, double[].class);
        want.array(ints).sizeEq(3).reflectionEq(new double[] { 12d, 45.6d, 78.9d });
    }
}
