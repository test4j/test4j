package org.test4j.hamcrest.iassert.object.impl;

import java.util.Iterator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

public class CollectionAssertTest_HasItem_DataProvider extends Test4J {

    @SuppressWarnings("rawtypes")
    public static Iterator provide_hasitems() {
        return new DataProvider() {
            {
                data(new Integer[]{1, 2, 3}, 1, new Integer[]{2});
                data(new Character[]{'a', 'b', 'c'}, 'a', new Character[]{'b'});
                data(new Boolean[]{true, false}, true, null);
                data(new Double[]{1.2d, 2.8d, 3.9d}, 1.2d, new Double[]{3.9d});
            }
        };
    }

    @ParameterizedTest
    @MethodSource("provide_hasitems")
    public void hasItems(Object[] actual, Object firstExpected, Object[] expected) {
        want.array(actual).hasAllItems(firstExpected, expected);
    }
}
