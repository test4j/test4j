package org.test4j.hamcrest.matcher.property;

import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.junit5.Test4J;
import org.test4j.model.GenicBean;
import org.test4j.model.User;
import org.test4j.tools.commons.ListHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class MapPropertyEqualMatcherTest extends Test4J {

    @Test
    public void testMatches() {
        User user = User.mock(123, "darui.wu");
        want.object(user).eqReflect(new DataMap() {
            {
                this.kv("name", "darui.wu");
            }
        });
    }

    @Test
    public void testMatches_IgnoreDefault() {
        User user = User.mock(123, "darui.wu");
        want.object(user).eqReflect(new DataMap() {
            {
                this.kv("name", "darui.wu");
                this.kv("id", null);
            }
        }, EqMode.IGNORE_DEFAULTS);
    }

    @Test
    public void testMatches_PropertyList() {
        GenicBean bean = GenicBean.newInstance("bean1", new String[]{"value1", "value2"});
        want.object(bean).eqReflect(new DataMap() {
            {
                // this.put("name", "bean1");
                this.kv("refObject", new String[]{"value1", "value2"});
            }
        });
    }

    @Test
    public void testMatches_PropertyList_IgnoreDefault() {
        GenicBean bean = GenicBean.newInstance("bean1", new String[]{"value1", "value2"});
        want.object(bean).eqReflect(new DataMap() {
            {
                // this.put("name", "bean1");
                this.kv("refObject", new String[]{null, "value1"});
            }
        }, EqMode.IGNORE_DEFAULTS, EqMode.IGNORE_ORDER);
    }

    @Test
    public void testMatches_PropertyList_Failure1() {
        GenicBean bean = GenicBean.newInstance("bean1", new String[]{"value1", "value2"});
        want.exception(() ->
                        want.object(bean).eqReflect(new DataMap() {
                            {
                                // this.put("name", "bean1");
                                this.kv("refObject", new String[]{null, "value1"});
                            }
                        }, EqMode.IGNORE_ORDER)
                , AssertionError.class);
    }

    @Test
    public void testMatches_PropertyList_Failure2() {
        GenicBean bean = GenicBean.newInstance("bean1", new String[]{"value1", "value2"});
        want.exception(() ->
                        want.object(bean).eqReflect(new DataMap() {
                            {
                                // this.put("name", "bean1");
                                this.kv("refObject", new String[]{null, "value1"});
                            }
                        }, EqMode.IGNORE_DEFAULTS)
                , AssertionError.class);
    }

    @Test
    public void testMatches_List_PoJo() {
        want.list(new User[]{User.mock(123, "darui.wu"), User.mock(124, "darui.wu")}).eqReflect(
                new DataMap(2) {
                    {
                        this.kv("name", "darui.wu");
                        this.kv("id", 123, 124);
                    }
                }, EqMode.IGNORE_ORDER);
    }

    @Test
    public void testMatches_List_Map() {
        want.list(ListHelper.toList(new HashMap() {
            {
                this.put("id", 123);
                this.put("name", "darui.wu");
            }
        }, new HashMap() {
            {
                this.put("id", 124);
                this.put("name", "darui.wu");
            }
        })).eqReflect(new DataMap(2) {
            {
                this.kv("name", "darui.wu");
                this.kv("id", 123, 124);
            }
        }, EqMode.IGNORE_ORDER);
    }

    @Test
    public void testReflectEqMap() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        want.exception(() ->
                        want.list(list).eqReflect(new DataMap()
                                .kv("id", 124)
                        )
                , AssertionError.class);
    }

    @Test
    public void testReflectEqMap2() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(new HashMap() {
            {
                this.put("id", 123);
            }
        });
        list.add(new HashMap() {
            {
                this.put("id", null);
            }
        });
        want.exception(() ->
                        want.list(list).eqReflect(new DataMap(2) {
                            {
                                this.kv("id", null, 124);
                            }
                        })
                , AssertionError.class);
    }

    @Test
    public void testReflectEqMap3() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(new HashMap() {
            {
                this.put("id", 123);
            }
        });
        list.add(new HashMap() {
            {
                this.put("id", null);
            }
        });
        want.exception(() ->
                        want.list(list).eqReflect(new DataMap(2) {
                            {
                                this.kv("id", null, 124);
                            }
                        }, EqMode.IGNORE_ORDER)
                , AssertionError.class);
    }
}
