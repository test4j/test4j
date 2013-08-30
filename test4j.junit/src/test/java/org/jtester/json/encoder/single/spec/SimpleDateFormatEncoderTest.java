package org.jtester.json.encoder.single.spec;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.json.encoder.single.spec.SimpleDateFormatEncoder;
import org.test4j.json.helper.JSONFeature;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleDateFormatEncoderTest implements JTester {
    @Test
    public void testEncode() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormatEncoder encoder = SimpleDateFormatEncoder.instance;
        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);

        StringWriter writer = new StringWriter();
        encoder.encode(df, writer, new ArrayList<String>());

        String json = writer.toString();
        want.string(json).isEqualTo("'yyyy-MM-dd'");
    }
}
