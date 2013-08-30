package org.jtester.json.decoder.single;

import java.util.HashMap;

import org.jtester.json.JSON;
import org.jtester.json.helper.JSONFeature;
import org.jtester.json.helper.JSONMap;
import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class BooleanDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        JSONMap json = new JSONMap() {
            private static final long serialVersionUID = 1L;
            {
                this.putJSON(JSONFeature.ValueFlag, true);
            }
        };
        BooleanDecoder decoder = BooleanDecoder.toBOOLEAN;
        Boolean bl = decoder.decode(json, Boolean.class, new HashMap<String, Object>());
        want.bool(bl).is(true);
    }

    @Test
    @DataFrom("simple_value")
    public void testSimpleValue(String json, boolean expected) {
        Boolean bool = JSON.toObject(json, Boolean.class);
        want.bool(bool).is(expected);
    }

    public static Object[][] simple_value() {
        return new Object[][] { { "0", false },// <br>
                { "'1'", true }, /** <br> */
                { "'tRue'", true }, /** <br> */
                { "\"true\"", true }, /** <br> */
                { "False", false }, /** <br> */
                { "false", false } /** <br> */
        };
    }
}
