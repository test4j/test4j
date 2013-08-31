package org.test4j.json.decoder.single;

import java.math.BigInteger;
import java.util.HashMap;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.json.helper.JSONFeature;
import org.test4j.json.helper.JSONMap;
import org.test4j.junit.Test4J;

public class BigIntegerDecoderTest extends Test4J {

    @SuppressWarnings("serial")
    @Test
    public void testDecodeSimpleValue() {
        JSONMap json = new JSONMap() {
            {
                this.putJSON(JSONFeature.ValueFlag, "1213435");
            }
        };
        BigIntegerDecoder decoder = BigIntegerDecoder.toBIGINTEGER;
        BigInteger bigInt = decoder.decode(json, BigInteger.class, new HashMap<String, Object>());
        want.number(bigInt).isEqualTo(new BigInteger("1213435"));
    }

    @Test
    public void testSimpleValue() {
        BigInteger bigInt = JSON.toObject("1213435", BigInteger.class);
        want.number(bigInt).isEqualTo(new BigInteger("1213435"));
    }
}
