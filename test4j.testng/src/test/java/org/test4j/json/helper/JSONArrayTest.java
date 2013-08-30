package org.test4j.json.helper;

import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "json" })
public class JSONArrayTest extends Test4J {

    @Test
    public void testDescription() {
        JSONArray array = new JSONArray();
        array.add(new JSONSingle("value"));

        String result = array.toString();
        want.string(result).isEqualTo("[value]");
    }
}
