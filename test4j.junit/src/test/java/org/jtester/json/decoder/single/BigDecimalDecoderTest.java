package org.jtester.json.decoder.single;

import java.math.BigDecimal;
import java.util.HashMap;

import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.json.decoder.single.BigDecimalDecoder;
import org.test4j.json.helper.JSONFeature;
import org.test4j.json.helper.JSONMap;

@SuppressWarnings("serial")
public class BigDecimalDecoderTest implements JTester {

    @Test
    public void testDecodeSimpleValue() {
        JSONMap json = new JSONMap() {
            {
                this.putJSON(JSONFeature.ValueFlag, "1213435");
            }
        };
        BigDecimalDecoder decoder = BigDecimalDecoder.toBIGDECIMAL;
        BigDecimal decimal = decoder.decode(json, BigDecimal.class, new HashMap<String, Object>());
        want.number(decimal).isEqualTo(new BigDecimal("1213435"));
    }

    @Test
    public void testSimpleValue() {
        BigDecimal decimal = JSON.toObject("1213435", BigDecimal.class);
        want.number(decimal).isEqualTo(new BigDecimal("1213435"));
    }
}
