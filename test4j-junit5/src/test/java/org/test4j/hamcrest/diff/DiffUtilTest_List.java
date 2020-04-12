package org.test4j.hamcrest.diff;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.module.core.utility.MessageHelper;

public class DiffUtilTest_List extends Test4J {
    @Test
    void test_list() {
        DiffMap diff = new DiffUtil(false, true, true).diff(
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
        want.string(diff.message()).contains(new String[]{"actual [1]", "actual [3]", "expect [2]", "expect [3]"});
    }

    @Test
    void test_ignore_null() {
        DiffMap diff = new DiffUtil(true, true, true).diff(
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
        want.string(diff.message()).contains(new String[]{"actual [3]", "expect [3]"});
    }
}
