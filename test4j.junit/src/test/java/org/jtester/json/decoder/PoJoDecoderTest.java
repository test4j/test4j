package org.jtester.json.decoder;

import java.util.Arrays;

import org.jtester.fortest.beans.Address;
import org.jtester.fortest.beans.User;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Test;

@SuppressWarnings("serial")
public class PoJoDecoderTest implements JTester {

    @Test
    public void testParseFromJSONMap() {
        String json = "{id:1,first:'wu',last:'darui'}";
        User user = JSON.toObject(json, User.class);
        want.object(user).reflectionEqMap(new DataMap() {
            {
                this.put("id", 1);
                this.put("first", "wu");
                this.put("last", "darui");
            }
        });
    }

    @Test
    public void testDecodePoJoArray() {
        String json = "[{id:1,first:'wu',last:'darui'},{id:2,first:'wu',last:'darui'}]";
        User[] users = JSON.toObject(json, User[].class);
        want.list(users).reflectionEqMap(2, new DataMap() {
            {
                this.put("id", 1, 2);
                this.put("first", "wu");
                this.put("last", "darui");
            }
        });
    }

    /**
     * 当属性是泛型时
     */
    @Test
    public void testDecodePoJo_PropIsGeneric() {
        String json = "{first:'wu', addresses: [{street:'凤起路',name:'杭州'}]}";
        User user = JSON.toObject(json, User.class);
        want.object(user).reflectionEq(
                new User().setFirst("wu").setAddresses(Arrays.asList(new Address("凤起路", null, "杭州"))),
                EqMode.IGNORE_DEFAULTS);
    }

    @Test
    public void testParseFromJSON_Primitive() {
        String json = "{_integer:1, _boolean:true, _double:4.5d, _float:5.4F}";
        PoJo pojo = JSON.toObject(json, PoJo.class);
        want.object(pojo).reflectionEq(new PoJo() {
            {
                _integer = 1;
                _boolean = true;
                _double = 4.5;
                _float = 5.4F;
            }
        });
    }
}

class PoJo {
    Object _integer;
    Object _boolean;
    Object _double;
    Object _float;
}
