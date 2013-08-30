package org.jtester.json.encoder;

import org.jtester.fortest.beans.Manager;
import org.jtester.json.JSON;
import org.jtester.json.helper.JSONFeature;
import org.jtester.junit.JTester;
import org.junit.Test;

public class PoJoEncoderTest implements JTester {
    String json = "";

    @Test
    public void testPoJoEncoder() {
        Manager manager = Manager.mock();
        this.json = JSON.toJSON(manager, JSONFeature.UseSingleQuote);
        want.string(json).contains("Tony Tester");
    }

    /**
     * 对象有多重继承的情况
     */
    @Test
    public void testPoJoDecoder() {
        Manager manager = JSON
                .toObject(
                        "{#class:'org.jtester.fortest.beans.Manager@cf710e',secretary:{#class:'org.jtester.fortest.beans.Employee@70be88',name:{#class:'string',#value:'Harry Hacker'},date:null},phoneNumber:{#class:'org.jtester.fortest.beans.PhoneNumber@9a9b65',code:{#class:'Integer',#value:571},number:{#class:'string',#value:'0571-88886666'}},name:{#class:'string',#value:'Tony Tester'},date:{#class:'Date',#value:'2012-09-12 14:06:36'}}",
                        Manager.class);
        want.object(manager).propertyEq("name", "Tony Tester");
    }
}
