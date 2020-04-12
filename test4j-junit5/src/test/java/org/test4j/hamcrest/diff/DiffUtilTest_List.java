package org.test4j.hamcrest.diff;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.module.core.utility.MessageHelper;

public class DiffUtilTest_List extends Test4J {
    @Test
    void test() {
        DiffMap diff = DiffUtil.diff(DataMap.create(4)
                        .kv("key1", "value11", "value12", "value13", "value14")
                        .kv("key2", "value21", "value22", "value23", "value24")
                        .rows(),
                DataMap.create(4)
                        .kv("key1", "value11", "value13", "value13", "value13")
                        .kv("key2", "value21", "value23", "value23", "value24")
                        .rows(),
                false, true, true);
        MessageHelper.info(diff.message());
    }
}
