package org.test4j.tools.datagen;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.List;

class KeyValueTest extends Test4J {

    @Test
    void values() {
        MyTestMap map = new MyTestMap(10)
                .testKey.values(1, 2, 6, 7, 9);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new int[]{1, 2, 6, 7, 9, 9, 9, 9, 9, 9});
    }

    @Test
    void testValues() {
        MyTestMap map = new MyTestMap(10)
                .testKey.values(index -> index - 4, 1, 2, 6, 7, 9);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new int[]{1, 1, 1, 1, 1, 2, 6, 7, 9, 9});
    }

    @Test
    void increase() {
        MyTestMap map = new MyTestMap(4)
                .testKey.increase(2, 2);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new int[]{2, 4, 6, 8});
    }

    @Test
    void autoIncrease() {
        MyTestMap map = new MyTestMap(4)
                .testKey.autoIncrease();
        List list = map.cols("test_column");
        want.list(list).eqReflect(new int[]{1, 2, 3, 4});
    }

    @Test
    void formatIncrease() {
        MyTestMap map = new MyTestMap(4)
                .testKey.formatIncrease("name_%d", 1, 1);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new String[]{"name_1", "name_2", "name_3", "name_4"});
    }

    @Test
    void formatAutoIncrease() {
        MyTestMap map = new MyTestMap(4)
                .testKey.formatAutoIncrease("name_%d");
        List list = map.cols("test_column");
        want.list(list).eqReflect(new String[]{"name_1", "name_2", "name_3", "name_4"});
    }

    @Test
    void functionIncrease() {
        MyTestMap map = new MyTestMap(4)
                .testKey.functionIncrease(index -> "name_" + index, 1, 1);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new String[]{"name_1", "name_2", "name_3", "name_4"});
    }

    @Test
    void functionAutoIncrease() {
        MyTestMap map = new MyTestMap(4)
                .testKey.functionAutoIncrease(index -> "name_" + index);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new String[]{"name_1", "name_2", "name_3", "name_4"});
    }

    @Test
    void functionObjs() {
        MyTestMap map = new MyTestMap(4)
                .testKey.functionObjs(obj -> "name_" + obj, 1, 3);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new String[]{"name_1", "name_3", "name_3", "name_3"});
    }

    @Test
    void loop() {
        MyTestMap map = new MyTestMap(4)
                .testKey.loop("name_1", "name_3");
        List list = map.cols("test_column");
        want.list(list).eqReflect(new String[]{"name_1", "name_3", "name_1", "name_3"});
    }

    @Test
    void generateBy() {
        MyTestMap map = new MyTestMap(4)
                .testKey.generateBy((index, arr) -> "name_" + arr[index % 2], 1, 3);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new String[]{"name_1", "name_3", "name_1", "name_3"});
    }

    @Test
    void generate() {
        MyTestMap map = new MyTestMap(4)
                .testKey.generate((index) -> "name_" + index);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new String[]{"name_0", "name_1", "name_2", "name_3"});
    }

    @Test
    void random() {
        MyTestMap map = new MyTestMap(4)
                .testKey.random();
        List list = map.cols("test_column");
        want.list(list).sizeEq(4);
    }

    @Test
    void testRandom() {
        MyTestMap map = new MyTestMap(4)
                .testKey.random(1);
        List list = map.cols("test_column");
        want.list(list).eqReflect(new int[]{1, 1, 1, 1});
    }

    private static class MyTestMap extends DataMap<MyTestMap> {
        public MyTestMap() {
        }

        public MyTestMap(int dataSize) {
            super(dataSize);
        }

        KeyValue<MyTestMap> testKey = new KeyValue<MyTestMap>(this, "test_column");
    }
}