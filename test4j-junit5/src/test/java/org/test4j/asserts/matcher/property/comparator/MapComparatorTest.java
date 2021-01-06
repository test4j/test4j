package org.test4j.asserts.matcher.property.comparator;

import org.junit.jupiter.api.Test;
import org.test4j.asserts.matcher.modes.EqMode;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;

import java.util.HashMap;

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
        want.object(new HashMap() {
            {
                this.put("id", 123);
                this.put("name", "darui.wu");
            }
        }).eqReflect(new HashMap() {
            {
                this.put("id", 123L);
                this.put("name", "darui.wu");
            }
        }, EqMode.IGNORE_DEFAULTS);
    }
}
