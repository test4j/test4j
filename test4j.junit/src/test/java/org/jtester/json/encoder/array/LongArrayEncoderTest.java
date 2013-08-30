package org.jtester.json.encoder.array;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.helper.JSONFeature;
import org.jtester.junit.JTester;
import org.junit.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LongArrayEncoderTest implements JTester {
    @Test
    public void testEncode() throws Exception {
        long[] values = new long[] { 1234, 4556L };

        JSONEncoder encoder = JSONEncoder.get(values.getClass());
        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

        StringWriter writer = new StringWriter();
        encoder.encode(values, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[1234, 4556]");
    }

    @Test
    public void testEncode_Long() throws Exception {
        Long[] values = new Long[] { 1234L, null };

        JSONEncoder encoder = JSONEncoder.get(values.getClass());
        want.object(encoder).clazIs(ObjectArrayEncoder.class);

        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

        StringWriter writer = new StringWriter();
        encoder.encode(values, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[1234, null]");
    }
}
