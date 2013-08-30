package org.jtester.json.encoder.object;

import org.jtester.fortest.beans.Manager;
import org.jtester.json.JSON;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.json.helper.JSONFeature;
import org.jtester.junit.JTester;
import org.junit.Test;

public class PoJoEncoderTest implements JTester {

    @Test
    public void testWrite_NormalPoJo() {
        User user = User.newInstance(3, "test user");

        String json = JSON.toJSON(user);

        want.string(json).contains(JSONFeature.ClazzFlag).contains(User.class.getName());
    }

    @Test
    public void testDecodeing() {
        String json = String.format("{\"%s\":\"%s\",\"name\":\"test user\",\"id\":3}", JSONFeature.ClazzFlag,
                User.class.getName());
        Object user = JSON.toObject(json);
        want.object(user).notNull().clazIs(User.class).propertyEq("name", "test user");
    }

    @Test
    public void testDecodeing_SpecClaz() {
        String json = "{\"#class\":\"" + User.class.getName() + "\",\"name\":\"test user\",\"id\":3}";
        Object user = JSON.toObject(json, User.class);
        want.object(user).notNull().clazIs(User.class).propertyEq("name", "test user");
    }

    @Test
    // "字段类型是接口时"
    public void testPoJoEncoder() {
        Manager manager = Manager.mock();
        String json = JSON.toJSON(manager, JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);
        want.string(json).contains("phoneNumber:{");
    }
}
