package org.jtester.json.helper;

import org.jtester.junit.JTester;
import org.junit.Test;

public class JSONArrayTest implements JTester {

    @Test
    public void testDescription() {
        JSONArray array = new JSONArray();
        array.add(new JSONSingle("value"));

        String result = array.toString();
        want.string(result).isEqualTo("[value]");
    }
}
