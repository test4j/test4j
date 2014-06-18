/*
 * Copyright 2013 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package org.test4j.json.decoder.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.json.helper.JSONFeature;
import org.test4j.junit.Test4J;

/**
 * 泛型对象反序列化测试
 * 
 * @author darui.wudr 2013-10-29 下午5:54:27
 */
@SuppressWarnings("serial")
public class GenericJsonDecoderTest extends Test4J {

    @Test
    public void testGenericList() {
        GenericList input = new GenericList() {
            {
                this.list = new ArrayList<GenericObj>() {
                    {
                        this.add(new GenericObj() {
                            {
                                this.name = "test";
                            }
                        });
                    }
                };
            }
        };
        String json = JSON.toJSON(input, JSONFeature.UnMarkClassFlag);
        want.string(json).isEqualTo("{list:[{name:\"test\"}]}");
        GenericList output = JSON.toObject(json, GenericList.class);
        want.object(output).reflectionEq(input);
        want.object(output.list.get(0)).clazIs(GenericObj.class);
    }

    @Test
    public void testGenericMap() {
        GenericMap input = new GenericMap() {
            {
                this.map = new HashMap<String, GenericObj>() {
                    {
                        this.put("name", new GenericObj() {
                            {
                                this.name = "test";
                            }
                        });
                    }
                };
            }
        };
        String json = JSON.toJSON(input, JSONFeature.UnMarkClassFlag);
        want.string(json).isEqualTo("{map:{\"name\":{name:\"test\"}}}");
        GenericMap output = JSON.toObject(json, GenericMap.class);
        want.object(output).reflectionEq(input);
        want.object(output.map.get("name")).clazIs(GenericObj.class);
    }

    @Test
    public void testGenericMyMap() {
        GenericMyMap input = new GenericMyMap() {
            {
                this.map = new MyMap<GenericObj>() {
                    {
                        this.put("name", new GenericObj() {
                            {
                                this.name = "test";
                            }
                        });
                    }
                };
            }
        };
        String json = JSON.toJSON(input, JSONFeature.UnMarkClassFlag);
        want.string(json).isEqualTo("{map:{\"name\":{name:\"test\"}}}");
        GenericMyMap output = JSON.toObject(json, GenericMyMap.class);
        want.object(output).reflectionEq(input);
        want.object(output.map.get("name")).clazIs(GenericObj.class);
    }

    @Test
    public void testHasFieldMap() {
        HasFieldMap map = new HasFieldMap() {
            {
                this.name = "test";
                this.put("key", "value");
            }
        };
        String json = JSON.toJSON(map, JSONFeature.UnMarkClassFlag);
        System.out.println(json);
    }

    public static class GenericList {
        List<GenericObj> list;
    }

    public static class GenericMap {
        Map<String, GenericObj> map;
    }

    public static class GenericMyMap {
        MyMap<GenericObj> map;
    }

    public static class GenericObj {
        String name;
    }

    public static class MyMap<T> extends HashMap<String, T> {
    }

    public static class HasFieldMap extends HashMap<String, String> {
        String name;
    }
}
