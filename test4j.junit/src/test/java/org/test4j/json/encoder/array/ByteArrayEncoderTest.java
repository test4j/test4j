package org.test4j.json.encoder.array;

import java.io.StringWriter;
import java.util.ArrayList;

import org.junit.Test;
import org.test4j.json.encoder.JSONEncoder;
import org.test4j.json.helper.JSONFeature;
import org.test4j.junit.Test4J;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ByteArrayEncoderTest extends Test4J {
    @Test
    public void testEncode() throws Exception {
        byte[] values = new byte[] { 48, 1, 127 };

        JSONEncoder encoder = JSONEncoder.get(values.getClass());
        StringWriter writer = new StringWriter();
        encoder.setFeatures(JSONFeature.UnMarkClassFlag);
        encoder.encode(values, writer, new ArrayList<String>());

        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[48,1,127]");
    }

    @Test
    public void testEncode_Byte() throws Exception {
        Byte[] values = new Byte[] { -128, null, 127 };

        JSONEncoder encoder = JSONEncoder.get(values.getClass());
        want.object(encoder).clazIs(ObjectArrayEncoder.class);

        StringWriter writer = new StringWriter();
        encoder.setFeatures(JSONFeature.UnMarkClassFlag);
        encoder.encode(values, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[-128,null,127]");
    }
}
