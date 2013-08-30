package org.test4j.json.decoder.array;

import org.test4j.json.JSON;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "json" })
public class IntArrayDecoderTest extends Test4J {

    @Test
    public void testParseFromJSONArray() {
        String json = "['1','2','3']";
        Integer[] ints = JSON.toObject(json, int[].class);
        want.array(ints).sizeEq(3).reflectionEq(new int[] { 1, 2, 3 });
    }

}
