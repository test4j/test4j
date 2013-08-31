package org.test4j.tools.datagen;

import java.util.Iterator;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.DataFrom;

@SuppressWarnings("rawtypes")
public class RandomDataGeneratorTest extends Test4J {

    @Test
    @DataFrom("dataRandom")
    public void testRandomDataGenerator(Class type, int index) {
        AbastractDataGenerator generator = RandomDataGenerator.random(type);
        Object o = generator.generate(index);
        want.object(o).notNull();
        System.out.println(o);
    }

    public static Iterator dataRandom() {
        return new DataIterator() {
            {
                data(Integer.class, 10);
                data(String.class, 3);
            }
        };
    }
}
