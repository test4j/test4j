package org.test4j.json.encoder.array;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.json.encoder.beans.test.User;
import org.test4j.json.helper.JSONFeature;
import org.test4j.junit.Test4J;

public class ArraysEncoderTest implements Test4J {

    @Test
    public void testNewInstance() {
        int[] myints = new int[] { 1, 2, 3, 4 };
        String json = JSON.toJSON(myints, JSONFeature.UseSingleQuote, JSONFeature.QuoteAllItems);
        want.string(json)
                .contains("'#class':'int[]@")
                .contains("'#value':[")
                .contains(
                        "[{'#class':'Integer','#value':1},{'#class':'Integer','#value':2},{'#class':'Integer','#value':3},{'#class':'Integer','#value':4}]");
    }

    @Test
    public void testArrayReference() {
        User[] users = new User[2];
        users[0] = User.newInstance(1, "darui.wu");
        users[1] = users[0];
        String json = JSON.toJSON(users, JSONFeature.UseSingleQuote);
        want.string(json).contains("#class:'org.test4j.json.encoder.beans.test.User@").contains("{#refer:@");
    }
}
