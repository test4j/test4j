package org.jtester.json.decoder.array;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

public class BooleanArrayDecoderTest implements JTester {

    @Test
    public void testParseFromJSONArray() {
        String json = "[1,false,'true',true]";
        Boolean[] ints = JSON.toObject(json, boolean[].class);
        want.array(ints).sizeEq(4).reflectionEq(new boolean[] { true, false, true, true });
    }
}
