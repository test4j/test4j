package org.test4j.json.decoder.single;

import java.io.File;
import java.net.URI;

import org.test4j.json.JSON;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "json" })
public class URIDecoderTest extends Test4J {

    @Test
    public void testDecodeSimpleValue() {
        String json = "{'#class':'URI','#value':'file:/d:/path/1.txt'}";
        URI uri = JSON.toObject(json);
        want.object(uri).isEqualTo(new File("d:/path/1.txt").toURI());
    }
}
