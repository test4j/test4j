package org.test4j.json.decoder.array;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class FloatArrayDecoderTest implements Test4J {

    @Test
    public void testParseFromJSONArray() {
        String json = "['12',12.4,\"45.6f\"]";
        Float[] ints = JSON.toObject(json, float[].class);
        want.array(ints).sizeEq(3).reflectionEq(new float[] { 12f, 12.4f, 45.6f });
    }
}
