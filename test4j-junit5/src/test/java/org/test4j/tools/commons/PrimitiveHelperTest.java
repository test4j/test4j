package org.test4j.tools.commons;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class PrimitiveHelperTest extends Test4J {

    @ParameterizedTest
    @MethodSource("testDoesEqualData")
    public void testDoesEqual(Number num1, Number num2, boolean result) {
        boolean actual = PrimitiveHelper.doesEqual(num1, num2);
        want.bool(actual).is(result);
    }

    public static Iterator testDoesEqualData() {
        return new DataProvider() {
            {
                data(1, 1L, true);
                data(Integer.valueOf(2), 2L, true);
                data(Long.valueOf(3), Short.valueOf("3"), true);
                data(4, 4.0, false);
                data(5.0d, 5.0f, true);
            }
        };
    }
}
