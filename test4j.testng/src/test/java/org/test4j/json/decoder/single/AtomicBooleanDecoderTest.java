package org.test4j.json.decoder.single;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.test4j.json.JSON;
import org.test4j.json.helper.JSONFeature;
import org.test4j.json.helper.JSONMap;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "json" })
public class AtomicBooleanDecoderTest extends Test4J {
    public void testAtomicBool() {
        AtomicBoolean expected = new AtomicBoolean(true);
        JSONMap json = new JSONMap() {
            private static final long serialVersionUID = 1L;

            {
                this.putJSON(JSONFeature.ValueFlag, 1);
            }
        };

        AtomicBoolean bool = JSON.toObject(json, AtomicBoolean.class, new HashMap<String, Object>());
        want.object(bool).reflectionEq(expected);
    }

    public void testAtomicBool_JSONString() {
        AtomicBoolean expected = new AtomicBoolean(true);

        AtomicBoolean bool = JSON.toObject("{'" + JSONFeature.ValueFlag + "':1}", AtomicBoolean.class);
        want.object(bool).reflectionEq(expected);
    }
}
