package org.test4j.json.decoder.single;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.json.helper.JSONFeature;
import org.test4j.json.helper.JSONMap;
import org.test4j.junit.Test4J;

@SuppressWarnings("serial")
public class BigDecimalDecoderTest extends Test4J {

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
