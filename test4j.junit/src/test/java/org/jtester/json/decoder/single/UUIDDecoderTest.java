package org.jtester.json.decoder.single;

import java.util.UUID;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

public class UUIDDecoderTest implements JTester {

    @Test
    public void testDecode() {
        UUID uuid = JSON.toObject("55c0c3cc-f816-4585-b16e-327eb4b58b77", UUID.class);
        want.string(uuid.toString()).isEqualTo("55c0c3cc-f816-4585-b16e-327eb4b58b77");
    }

    @Test
    public void testDecode_WithClazzFlag() {
        String json = "{'#class':'UUID','#value':'bbab311f-06c2-4ed8-83cf-282be94418eb'}";
        UUID uuid = JSON.toObject(json, UUID.class);
        want.string(uuid.toString()).isEqualTo("bbab311f-06c2-4ed8-83cf-282be94418eb");
    }
}
