package org.test4j.tools.generic;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author darui.wudr 2013-10-29 下午8:29:49
 */
@SuppressWarnings({ "serial" })
public class GenericTypeFinderTest extends Test4J {
    @Test
    public void testGenericField() {
        GenericTypeMap map = GenericTypeFinder.findGenericTypes(GenericMap.class);
        want.map(map).eqReflect(new GenericTypeMap() {
            {
                this.putType(AbstractMap.class, "K", String.class);
                this.putType(AbstractMap.class, "V", GenericObj.class);
                this.putType(HashMap.class, "K", String.class);
                this.putType(HashMap.class, "V", GenericObj.class);
                this.putType(Map.class, "K", String.class);
                this.putType(Map.class, "V", GenericObj.class);
                this.putType(MyMap.class, "T", GenericObj.class);
            }
        });
    }

    public static class GenericObj {
        String name;
    }

    public static class MyMap<T> extends HashMap<String, T> {
    }

    public static class GenericMap extends MyMap<GenericObj> {
    }

    public static class GenericParameterMap extends HashMap<String, Type> {

    }
}
