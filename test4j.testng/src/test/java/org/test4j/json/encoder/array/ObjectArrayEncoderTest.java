package org.test4j.json.encoder.array;

import java.io.StringWriter;
import java.util.ArrayList;

import org.test4j.json.encoder.JSONEncoder;
import org.test4j.json.encoder.beans.test.User;
import org.test4j.json.helper.JSONFeature;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Test(groups = { "test4j", "json" })
public class ObjectArrayEncoderTest extends Test4J {
    @Test
    public void testEncode() throws Exception {
        User[] users = new User[] { User.newInstance(12, "darui.wu"), null };

        JSONEncoder encoder = JSONEncoder.get(users.getClass());
        want.object(encoder).clazIs(ObjectArrayEncoder.class);

        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);
        StringWriter writer = new StringWriter();
        encoder.encode(users, writer, new ArrayList<String>());

        String json = writer.toString();
        want.string(json).eqIgnoreSpace("[{id:12,name:'darui.wu',age:0,salary:0,isFemale:false},null]");
    }
}
