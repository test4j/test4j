package org.test4j.json.encoder.array;

import java.io.StringWriter;
import java.util.ArrayList;

import org.junit.Test;
import org.test4j.json.encoder.JSONEncoder;
import org.test4j.json.helper.JSONFeature;
import org.test4j.junit.Test4J;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IntegerArrayEncoderTest extends Test4J {
    @Test
    public void testEncode() throws Exception {
        int[] ints = new int[] { 1, 2, 3 };

        JSONEncoder encoder = JSONEncoder.get(ints.getClass());
        encoder.setFeatures(JSONFeature.UnMarkClassFlag);
        StringWriter writer = new StringWriter();
        encoder.encode(ints, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[1,2,3]");
    }

    @Test
    public void testEncode_Integer() throws Exception {
        Integer[] ints = new Integer[] { 1, null, 3 };

        JSONEncoder encoder = JSONEncoder.get(ints.getClass());
        encoder.setFeatures(JSONFeature.UnMarkClassFlag);
        want.object(encoder).clazIs(ObjectArrayEncoder.class);

        StringWriter writer = new StringWriter();
        encoder.encode(ints, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[1,null,3]");
    }
}
