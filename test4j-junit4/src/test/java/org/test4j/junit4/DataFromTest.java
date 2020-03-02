package org.test4j.junit4;

import org.junit.Test;
import org.test4j.tools.datagen.DataProvider;

public class DataFromTest extends Test4J {
    @DataFrom("dataForTest")
    @Test
    public void testDataFrom(int input, int expected) {
        want.number(input).eq(expected);
    }

    public static DataProvider dataForTest() {
        return new DataProvider()
                .data(1, 1)
                .data(2, 2);
    }
}
