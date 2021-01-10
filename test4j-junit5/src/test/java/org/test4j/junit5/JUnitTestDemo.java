package org.test4j.junit5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.ICore;
import org.test4j.Test4J;
import org.test4j.tools.datagen.DataProvider;

/**
 * 简单junit4测试示例
 */
public class JUnitTestDemo implements Test4J {
    @Test
    public void demo() {
        int count = 10;
        ICore.want.number(count).isEqualTo(10);
    }

    @ParameterizedTest
    @MethodSource("dataForDataFrom")
    void testDataFrom(String actual, String expected) {
        want.string(actual).eq(expected);
    }

    public static DataProvider dataForDataFrom() {
        return new DataProvider() {
            {
                data("string", "string");
                data("we", "we");
            }
        };
    }
}