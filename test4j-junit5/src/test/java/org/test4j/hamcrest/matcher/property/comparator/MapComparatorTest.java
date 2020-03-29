package org.test4j.hamcrest.matcher.property.comparator;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;

@SuppressWarnings({"rawtypes", "unchecked", "serial"})
public class MapComparatorTest extends Test4J {
    @Test
    public void testMap() {
        want.object(new HashMap() {
            {
                this.put("id", 123);
                this.put("name", "darui.wu");
            }
        }).eqReflect(new HashMap() {
            {
                this.put("id", 123);
                this.put("name", null);
            }
        }, EqMode.IGNORE_DEFAULTS);
    }

    @Test
    public void testMap2() {
        want.object(User.mock(123, "darui.wu")).eqReflect(new DataMap() {
            {
                this.kv("id", 123);
                this.kv("name", null);
            }
        }, EqMode.IGNORE_DEFAULTS);
    }

    @Test
    public void testMap3() {
        want.exception(() ->
                        want.object(new HashMap() {
                            {
                                this.put("id", 123);
                                this.put("name", "darui.wu");
                            }
                        }).eqReflect(new HashMap() {
                            {
                                this.put("id", 123);
                            }
                        }, EqMode.IGNORE_DEFAULTS)
                , AssertionError.class);
    }

}
