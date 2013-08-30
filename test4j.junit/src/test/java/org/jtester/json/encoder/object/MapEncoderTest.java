package org.jtester.json.encoder.object;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.json.encoder.JSONEncoder;
import org.test4j.json.encoder.object.MapEncoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapEncoderTest implements JTester {

    @Test
    public void testGetPropertyEncoders() throws Exception {
        Map<String, String> map = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                this.put("key1", "value1");
                this.put("key2", "value2");
            }
        };
        StringWriter writer = new StringWriter();
        JSONEncoder encoder = new MapEncoder(HashMap.class);
        encoder.encode(map, writer, new ArrayList<String>());
        String json = writer.toString();
        System.out.println("JONS: " + json);
    }
}
