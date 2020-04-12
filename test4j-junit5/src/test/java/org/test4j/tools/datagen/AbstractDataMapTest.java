package org.test4j.tools.datagen;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractDataMapTest extends Test4J {

    @Test
    void rows() {
        List list = DataMap.create(2)
                .kv("key1", "value11", "value12")
                .kv("key2", "value21", "value22")
                .rows(HashMap.class);
        want.list(list).eqReflect(DataMap.create(2)
                .kv("key1", "value11", "value12")
                .kv("key2", "value21", "value22")
                .rows()
        );
    }
}