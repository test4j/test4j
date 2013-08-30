package org.test4j.tools.datagen;

import java.util.Iterator;

import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
@Test(groups = { "test4j", "tools" })
public class RandomDataGeneratorTest extends Test4J {

    @Test(dataProvider = "dataRandom")
    public void testRandomDataGenerator(Class type, int index) {
        AbastractDataGenerator generator = RandomDataGenerator.random(type);
        Object o = generator.generate(index);
        want.object(o).notNull();
        System.out.println(o);
    }

    @DataProvider
    public Iterator dataRandom() {
        return new DataIterator() {
            {
                data(Integer.class, 10);
                data(String.class, 3);
            }
        };
    }
}
