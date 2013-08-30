package org.jtester.tools.datagen;

import java.util.Iterator;

import org.jtester.testng.JTester;
import org.test4j.tools.datagen.AbastractDataGenerator;
import org.test4j.tools.datagen.RandomDataGenerator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
@Test(groups = { "jtester", "tools" })
public class RandomDataGeneratorTest extends JTester {

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
