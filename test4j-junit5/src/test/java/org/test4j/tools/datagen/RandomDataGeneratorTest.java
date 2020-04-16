package org.test4j.tools.datagen;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;

import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class RandomDataGeneratorTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataRandom")
    public void testRandomDataGenerator(Class type, int index) {
        AbstractDataGenerator generator = RandomDataGenerator.random(type);
        Object o = generator.generate(index);
        want.object(o).notNull();
        System.out.println(o);
    }

    public static Iterator dataRandom() {
        return new DataProvider() {
            {
                data(Integer.class, 10);
                data(String.class, 3);
            }
        };
    }
}
