package org.test4j.json.encoder.array;

import java.io.StringWriter;
import java.util.ArrayList;

import org.test4j.json.encoder.JSONEncoder;
import org.test4j.json.helper.JSONFeature;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Test(groups = { "test4j", "json" })
public class ShortArrayEncoderTest extends Test4J {
    @Test
    public void testEncode() throws Exception {
        short[] values = new short[] { 1234, Short.valueOf("4567") };

        JSONEncoder encoder = JSONEncoder.get(values.getClass());

        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

        StringWriter writer = new StringWriter();
        encoder.encode(values, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[1234, 4567]");
    }

    @Test
    public void testEncode_Short() throws Exception {
        Short[] values = new Short[] { 1234, null };

        JSONEncoder encoder = JSONEncoder.get(values.getClass());
        want.object(encoder).clazIs(ObjectArrayEncoder.class);

        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

        StringWriter writer = new StringWriter();
        encoder.encode(values, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[1234, null]");
    }
}
