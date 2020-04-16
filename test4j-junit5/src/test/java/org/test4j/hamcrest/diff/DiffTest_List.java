package org.test4j.hamcrest.diff;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.module.core.utility.MessageHelper;

public class DiffTest_List extends Test4J {
    @Test
    void test_list() {
        DiffMap diff = new DiffFactory(false, true, true).diff(
                DataMap.create(4)
                        .kv("key1", "value11", "value12", "value13", "value14")
                        .kv("key2", "value21", "value22", "value23", "value24")
                        .rows(),
                DataMap.create(4)
                        .kv("key1", "value11", "value13", "value13", "value13")
                        .kv("key2", "value21", "value23", "value23", "value24")
                        .rows());
        MessageHelper.info(diff.message());
        want.number(diff.diff).isGt(0);
        want.string(diff.message()).contains(new String[]{
                "$[3]~[2].key1", "$[3]~[2].key2", "$[4]~[4].key1"
        });
    }

    @Test
    void test_ignore_null() {
        DiffMap diff = new DiffFactory(true, true, true).diff(
                DataMap.create(4)
                        .kv("key1", "value11", "value12", "value13", "value14")
                        .kv("key2", "value21", "value22", "value23", "value24")
                        .rows(),
                DataMap.create(4)
                        .kv("key1", "value11", "value13", "value12", "value13")
                        .kv("key2", "value21", "value23", null, "value24")
                        .rows()
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isGt(0);
        want.string(diff.message()).contains(new String[]{"$[4]~[4].key1", "(String) value14", "(String) value13"});
    }

    @Test
    void test_nest_map() {
        DiffMap diff = new DiffFactory(true, true, true).diff(
                DataMap.create(2)
                        .kv("key1", "value11",
                                DataMap.create(2)
                                        .kv("nest_key1", "nest1", "nest2")
                                        .kv("nest_key2", "nest3", "nest4")
                                        .rows()
                        )
                        .rows(),
                DataMap.create(2)
                        .kv("key1", "value11",
                                DataMap.create(2)
                                        .kv("nest_key1", "nest1", "nest2")
                                        .kv("nest_key2", "nest3", "nest5")
                                        .rows()
                        )
                        .rows()
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isGt(0);
        want.string(diff.message()).contains(new String[]{"$[2]~[2].key1[2]~[2].nest_key2", "(String) nest4", "(String) nest5"});
    }
}
