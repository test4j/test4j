package org.test4j.asserts.matcher.property;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.test4j.asserts.matcher.modes.EqMode;
import org.test4j.junit5.Test4J;
import org.test4j.tools.commons.DateHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"unchecked", "serial", "rawtypes"})
public class MapListPropertyEqualMatcherTest extends Test4J {
    @Test
    public void testMapListPropertyEqualMatcher() {
        List<Map<String, ?>> expected = new ArrayList() {
            {
                this.add(new HashMap() {
                    {
                        this.put("id", 123);
                    }
                });
                this.add(new HashMap() {
                    {
                        this.put("name", "jobs.he");
                    }
                });
            }
        };
        ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected,
                new EqMode[]{EqMode.IGNORE_DEFAULTS});

        List<Map<String, ?>> actual = new ArrayList<Map<String, ?>>() {
            {
                this.add(new HashMap() {
                    {
                        this.put("id", 123);
                        this.put("name", "darui.wu");
                    }
                });
                this.add(new HashMap() {
                    {
                        this.put("id", 124);
                        this.put("name", "jobs.he");
                    }
                });
            }
        };
        MatcherAssert.assertThat(actual, matcher);
    }

    @Test
    public void testMapListPropertyEqualMatcher2() {
        DataMap expected = new DataMap(2) {
            {
                this.kv("id", 123, 124);
                this.kv("name", null, "jobs.he");
            }
        };

        ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected,
                new EqMode[]{EqMode.IGNORE_DEFAULTS});

        List<Map<String, ?>> actual = new ArrayList<Map<String, ?>>() {
            {
                this.add(new HashMap() {
                    {
                        this.put("id", 123);
                        this.put("name", "darui.wu");
                    }
                });
                this.add(new HashMap() {
                    {
                        this.put("id", 124);
                        this.put("name", "jobs.he");
                    }
                });
            }
        };
        MatcherAssert.assertThat(actual, matcher);
    }

    @Test
    public void testPropEqString() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(mockMap());
        list.add(mockMap());
        want.list(list).eqReflect(new DataMap(2) {
            {
                this.kv("integer", "20");
                this.kv("boolean", "true");
                this.kv("double", "20.0");
                this.kv("date", "2011-11-12");
            }
        }, EqMode.EQ_STRING);
    }

    private Map<String, Object> mockMap() {
        return new HashMap<String, Object>() {
            {
                this.put("integer", 20);
                this.put("boolean", true);
                this.put("double", 20D);
                this.put("date", DateHelper.parse("2011-11-12"));
            }
        };
    }
}
