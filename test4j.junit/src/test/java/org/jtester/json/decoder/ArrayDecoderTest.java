package org.jtester.json.decoder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jtester.json.JSON;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class ArrayDecoderTest implements JTester {
    @Test
    public void testDecode_Reference() {
        String json = "[{#class:org.jtester.json.encoder.beans.test.User@19762f,id:{#class:Integer,'#value':1},name:{#class:string,'#value':'darui.wu'},age:{#class:Integer,'#value':0},salary:{#class:Double,'#value':0},isFemale:{#class:Boolean,'#value':false}},{#refer:@19762f}]";
        User[] users = JSON.toObject(json, User[].class);
        want.array(users).sizeEq(2);
        want.object(users[0]).same(users[1]);
    }

    @Test
    public void testDecode() {
        String json = "{#class:org.jtester.json.encoder.beans.test.User@19762f,id:{#class:Integer,'#value':1},name:{#class:string,'#value':'darui.wu'},age:{#class:Integer,'#value':0},salary:{#class:Double,'#value':0},isFemale:{#class:Boolean,'#value':false}}";

        User[] users = JSON.toObject("[" + json + "," + json + "]", User[].class);
        want.array(users).sizeEq(2);
        want.object(users[0]).reflectionEq(users[1]);
    }

    @Test
    public void testDecode_ArrayRef() {
        String json = "{'key1':{#class:'[I@123',#value:[1,2,3]},'key2':{#refer:@123}}";
        Map<String, Integer[]> map = JSON.toObject(json);
        want.map(map).sizeEq(2).hasKeys("key1", "key2");
        Integer[] ia1 = map.get("key1");
        Integer[] ia2 = map.get("key2");
        want.object(ia1).reflectionEq(new int[] { 1, 2, 3 }).same(ia2);
    }

    @Test
    @DataFrom("dataForGetArray")
    public void testGetArray(Class type, Class expected) {
        Object o = reflector.invoke(ArrayDecoder.toARRAY, "getArray", type);
        want.object(o).clazIs(expected);
    }

    public static Iterator dataForGetArray() {
        return new DataIterator() {
            {
                data(int[].class, Integer[].class);
                data(int[][].class, Integer[][].class);
                data(short[][][].class, Short[][][].class);
                data(List[].class, List[].class);
                data(List[][].class, List[][].class);
                data(Map[].class, Map[].class);
                data(HashMap[][].class, HashMap[][].class);
            }
        };
    }

    @Test
    @DataFrom("dataForGetComponent")
    public void testGetComponent(String toTypeName, Class componentType) throws Exception {
        Type toType = ForTestType.getType(toTypeName);
        Type type = reflector.invoke(ArrayDecoder.toARRAY, "getComponent", toType);
        want.object(type).isEqualTo(componentType);
    }

    public static Iterator dataForGetComponent() {
        return new DataIterator() {
            {
                data("objects", HashMap.class);
                data("users", User.class);
            }
        };
    }
}
