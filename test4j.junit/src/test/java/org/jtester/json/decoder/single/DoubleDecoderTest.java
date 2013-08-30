package org.jtester.json.decoder.single;

import java.util.HashMap;

import org.jtester.json.JSON;
import org.jtester.json.helper.JSONMap;
import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class DoubleDecoderTest implements JTester {
    @Test
    public void testDecodeSimpleValue() {
        JSONMap json = new JSONMap() {
            private static final long serialVersionUID = 1L;
            {
                this.putJSON("#value", 23243.34d);
            }
        };
        DoubleDecoder decoder = DoubleDecoder.toDOUBLE;
        Double d = decoder.decode(json, Double.class, new HashMap<String, Object>());
        want.number(d).isEqualTo(23243.34d);
    }

    @Test
    @DataFrom("simple_value")
    public void testSimpleValue(String json, double expected) {
        Double actual = JSON.toObject(json, Double.class);
        want.number(actual).isEqualTo(expected);
    }

    public static Object[][] simple_value() {
        return new Object[][] { { "34234.34d", 34234.34d },// <br>
                { "'34234.34d'", 34234.34d }, /** <br> */
                { "34234.34", 34234.34d } /** <br> */
        };
    }
}
