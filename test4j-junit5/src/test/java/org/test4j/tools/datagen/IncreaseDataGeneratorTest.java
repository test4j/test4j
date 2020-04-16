package org.test4j.tools.datagen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class IncreaseDataGeneratorTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataIncrease")
    public void testIncreaseDataGenerator(Number from, Number step, int index, Number expected) {
        AbstractDataGenerator generator = IncreaseDataGenerator.increase(from, step);
        Object result = generator.generate(index);
        want.object(result).isEqualTo(expected);
    }

    public static Iterator dataIncrease() {
        return new DataProvider() {
            {
                data(4, 1, 10, 14);
                data(2L, 2L, 5, 12L);
                data(Short.valueOf("1"), Short.valueOf("2"), 10, Short.valueOf("21"));
                data(1.0d, 2.0d, 5, 11.0d);
                data(1.0f, 2.0f, 4, 9.0f);

                data(BigInteger.valueOf(1), BigInteger.valueOf(3), 2, BigInteger.valueOf(7));
                data(BigDecimal.valueOf(4.0), BigDecimal.valueOf(3), 0, BigDecimal.valueOf(4.0));
            }
        };
    }

    @Test
    public void testIncreaseDataGenerator_Failure() {
        AbstractDataGenerator generator = IncreaseDataGenerator.increase(1, 2.0);
        Object result = generator.generate(3);
        want.object(result).isEqualTo(7);
    }
}
