package org.test4j.json.decoder.base;

import java.util.HashMap;
import java.util.Map;

import org.test4j.json.JSON;
import org.test4j.json.decoder.PoJoDecoder;
import org.test4j.json.encoder.beans.test.User;
import org.test4j.json.helper.JSONArray;
import org.test4j.json.helper.JSONFeature;
import org.test4j.json.helper.JSONMap;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SuppressWarnings({ "serial" })
@Test(groups = { "test4j", "json" })
public class MapPoJoBaseDecoderTest extends Test4J {

    @Test
    public void testDecode() {
        JSONMap json = new JSONMap() {
            {
                this.putJSON(JSONFeature.ClazzFlag, "org.test4j.json.encoder.beans.test.User@a123b");
                this.putJSON("name", "darui.wu");
            }
        };

        Map<String, Object> references = new HashMap<String, Object>();
        User user = (User) PoJoDecoder.toPOJO.decode(json, User.class, references);
        want.object(user).propertyEq("name", "darui.wu");
        want.map(references).hasEntry("@a123b", user);
    }

    @Test(description = "json数组，数组的值指向同一个对象")
    public void testDecode_withRefObj() {
        User[] t = new User[2];
        System.out.println(t.getClass().getName());
        JSONMap json = new JSONMap() {
            {
                this.putJSON(JSONFeature.ClazzFlag, "[Lorg.test4j.json.encoder.beans.test.User;@01");
                this.putJSON(JSONFeature.ValueFlag, new JSONArray() {
                    {
                        this.add(new JSONMap() {
                            {
                                this.putJSON(JSONFeature.ClazzFlag, "org.test4j.json.encoder.beans.test.User@11");
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
