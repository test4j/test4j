package org.jtester.tools.datagen;

import java.util.Iterator;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class RepeatDataGeneratorTest implements JTester {

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
