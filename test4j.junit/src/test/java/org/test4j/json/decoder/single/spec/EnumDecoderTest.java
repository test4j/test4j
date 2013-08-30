package org.test4j.json.decoder.single.spec;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.json.helper.JSONFeature;
import org.test4j.junit.Test4J;

public class EnumDecoderTest implements Test4J {

    @Test
    public void testDecode() {
        String json = String.format("{'#class':'%s','#value':%s}", JSONFeature.class.getName(),
                JSONFeature.UnMarkClassFlag);

        Object o = JSON.toObject(json);
        want.object(o).clazIs(JSONFeature.class).isEqualTo(JSONFeature.UnMarkClassFlag);
    }

    @Test
    public void testDecode2() {
        String json = JSONFeature.UnMarkClassFlag.name();

        Object o = JSON.toObject(json, JSONFeature.class);
        want.object(o).clazIs(JSONFeature.class).isEqualTo(JSONFeature.UnMarkClassFlag);
    }
}
