package org.test4j.json.decoder.array;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.JTester;

public class BooleanArrayDecoderTest implements JTester {

    @Test
    public void testParseFromJSONArray() {
        String json = "[1,false,'true',true]";
        Boolean[] ints = JSON.toObject(json, boolean[].class);
        want.array(ints).sizeEq(4).reflectionEq(new boolean[] { true, false, true, true });
    }
}
