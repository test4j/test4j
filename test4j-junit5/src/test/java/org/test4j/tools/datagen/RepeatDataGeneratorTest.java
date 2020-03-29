package org.test4j.tools.datagen;

import java.util.Iterator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;

@SuppressWarnings("rawtypes")
public class RepeatDataGeneratorTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataRepeat")
    public void testRepeatDataGenerator(int index, Object expected) {
        AbstractDataGenerator generator = RepeatDataGenerator.repeat("a", "b", "c", "d");
        Object o = generator.generate(index);
        want.object(o).isEqualTo(expected);
    }

    public static Iterator dataRepeat() {
        return new DataProvider() {
            {
                data(0, "a");
                data(3, "d");
                data(4, "a");
                data(5, "b");
                data(11, "d");
            }
        };
    }
}
