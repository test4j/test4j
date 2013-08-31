package org.test4j.tools.datagen;

import java.util.Iterator;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.DataFrom;

@SuppressWarnings("rawtypes")
public class RepeatDataGeneratorTest extends Test4J {

    @Test
    @DataFrom("dataRepeat")
    public void testRepeatDataGenerator(int index, Object expected) {
        AbastractDataGenerator generator = RepeatDataGenerator.repeat("a", "b", "c", "d");
        Object o = generator.generate(index);
        want.object(o).isEqualTo(expected);
    }

    public static Iterator dataRepeat() {
        return new DataIterator() {
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
