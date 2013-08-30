package org.test4j.json.encoder;

import org.junit.Test;
import org.test4j.fortest.beans.Manager;
import org.test4j.json.JSON;
import org.test4j.json.helper.JSONFeature;
import org.test4j.junit.Test4J;

public class PoJoEncoderTest implements Test4J {
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
                        "{#class:'org.test4j.fortest.beans.Manager@cf710e',secretary:{#class:'org.test4j.fortest.beans.Employee@70be88',name:{#class:'string',#value:'Harry Hacker'},date:null},phoneNumber:{#class:'org.test4j.fortest.beans.PhoneNumber@9a9b65',code:{#class:'Integer',#value:571},number:{#class:'string',#value:'0571-88886666'}},name:{#class:'string',#value:'Tony Tester'},date:{#class:'Date',#value:'2012-09-12 14:06:36'}}",
                        Manager.class);
        want.object(manager).propertyEq("name", "Tony Tester");
    }
}
