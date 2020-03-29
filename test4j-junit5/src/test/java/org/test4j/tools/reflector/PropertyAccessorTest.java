package org.test4j.tools.reflector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;
import org.test4j.tools.datagen.DataProvider;

@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class PropertyAccessorTest extends Test4J {

    @ParameterizedTest
    @MethodSource("testGetPropertyDatas")
    public void testGetPropertyByOgnl(Object target, String property, String expected) {
        String value = (String) PropertyAccessor.getPropertyByOgnl(target, property, true);
        want.string(value).isEqualTo(expected);
    }

    public static Iterator testGetPropertyDatas() {
        return new DataProvider() {
            {
                data(new HashMap() {
                    {
                        this.put("key1", "value1");
                        this.put("key2", "value2");
                    }
                }, "key1", "value1");

                data(new HashMap() {
                    {
                        this.put("key1", User.mock(12, "darui.wu"));
                        this.put("key2", "value2");
                    }
                }, "key1.name", "darui.wu");

                data(User.mock(12, "darui.wu"), "name", "darui.wu");

                data(new HashMap() {
                    {
                        put("map1.key1", new HashMap() {
                            {
                                put("map2.key2", "ok");
                            }
                        });
                    }
                }, "map1.key1.map2.key2", "ok");
            }
        };
    }

    @Test
    public void testGetPropertyByOgnl_NoKey_Failure() {
        Map target = new HashMap() {
            {
                put("map1.key1", new HashMap() {
                    {
                        put("map2.key2", "ok");
                    }
                });
            }
        };
        want.exception(() ->
                        PropertyAccessor.getPropertyByOgnl(target, "map1.key1.map2.key1", true),
                NoSuchFieldRuntimeException.class);
    }

    @Test
    public void testGetPropertyByOgnl_NoKey_Success() {
        Map target = new HashMap() {
            {
                put("map1.key1", new HashMap() {
                    {
                        put("map2.key2", "ok");
                    }
                });
            }
        };
        Object value = PropertyAccessor.getPropertyByOgnl(target, "map1.key1.map2.key1", false);
        want.object(value).same(target);
    }
}
