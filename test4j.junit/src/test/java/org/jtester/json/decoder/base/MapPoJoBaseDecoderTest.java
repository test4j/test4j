package org.jtester.json.decoder.base;

import java.util.HashMap;
import java.util.Map;

import org.jtester.json.JSON;
import org.jtester.json.decoder.PoJoDecoder;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.json.helper.JSONArray;
import org.jtester.json.helper.JSONFeature;
import org.jtester.json.helper.JSONMap;
import org.jtester.junit.JTester;
import org.junit.Test;

@SuppressWarnings({ "serial" })
public class MapPoJoBaseDecoderTest implements JTester {

    @Test
    public void testDecode() {
        JSONMap json = new JSONMap() {
            {
                this.putJSON(JSONFeature.ClazzFlag, "org.jtester.json.encoder.beans.test.User@a123b");
                this.putJSON("name", "darui.wu");
            }
        };

        Map<String, Object> references = new HashMap<String, Object>();
        User user = (User) PoJoDecoder.toPOJO.decode(json, User.class, references);
        want.object(user).propertyEq("name", "darui.wu");
        want.map(references).hasEntry("@a123b", user);
    }

    @Test
    // description = "json数组，数组的值指向同一个对象"
    public void testDecode_withRefObj() {
        User[] t = new User[2];
        System.out.println(t.getClass().getName());
        JSONMap json = new JSONMap() {
            {
                this.putJSON(JSONFeature.ClazzFlag, "[Lorg.jtester.json.encoder.beans.test.User;@01");
                this.putJSON(JSONFeature.ValueFlag, new JSONArray() {
                    {
                        this.add(new JSONMap() {
                            {
                                this.putJSON(JSONFeature.ClazzFlag, "org.jtester.json.encoder.beans.test.User@11");
                                this.putJSON("name", "darui.wu");
                            }
                        });
                        this.add(new JSONMap() {
                            {
                                this.putJSON(JSONFeature.ReferFlag, "@11");
                            }
                        });
                    }
                });
            }
        };

        Map<String, Object> references = new HashMap<String, Object>();
        User[] users = JSON.toObject(json, references);
        want.array(users).sizeEq(2).propertyEq("name", new String[] { "darui.wu", "darui.wu" });
        want.object(users[0]).same(users[1]);
    }
}
