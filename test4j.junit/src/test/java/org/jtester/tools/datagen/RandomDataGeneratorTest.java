package org.jtester.tools.datagen;

import java.util.Iterator;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class RandomDataGeneratorTest implements JTester {

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
