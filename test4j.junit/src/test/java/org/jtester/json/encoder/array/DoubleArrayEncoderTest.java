package org.jtester.json.encoder.array;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.helper.JSONFeature;
import org.jtester.junit.JTester;
import org.junit.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DoubleArrayEncoderTest implements JTester {
    @Test
    public void testEncode() throws Exception {
        double[] values = new double[] { 12.34d, 45.56D };

        JSONEncoder encoder = JSONEncoder.get(values.getClass());
        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

        StringWriter writer = new StringWriter();
        encoder.encode(values, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[12.34, 45.56]");
    }

    @Test
    public void testEncode_Double() throws Exception {
        Double[] values = new Double[] { 12.34, null };

        JSONEncoder encoder = JSONEncoder.get(values.getClass());
        want.object(encoder).clazIs(ObjectArrayEncoder.class);

        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

        StringWriter writer = new StringWriter();
        encoder.encode(values, writer, new ArrayList<String>());
        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[12.34, null]");
    }
}
