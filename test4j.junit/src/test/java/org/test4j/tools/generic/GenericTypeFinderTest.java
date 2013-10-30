/*
 * Copyright 2013 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package org.test4j.tools.generic;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;

import org.junit.Test;
import org.test4j.junit.Test4J;

/**
 * @author darui.wudr 2013-10-29 下午8:29:49
 */
@SuppressWarnings({ "serial" })
public class GenericTypeFinderTest extends Test4J {
    @Test
    public void testGenericField() {
        GenericTypeMap map = GenericTypeFinder.findGenericTypes(GenericMap.class);
        want.map(map).reflectionEq(new GenericTypeMap() {
            {
                this.putType(AbstractMap.class, "K", String.class);
                this.putType(AbstractMap.class, "V", GenericObj.class);
                this.putType(HashMap.class, "K", String.class);
                this.putType(HashMap.class, "V", GenericObj.class);
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
