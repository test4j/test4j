package org.test4j.tools.datagen;

import java.util.Iterator;

import org.test4j.testng.JTester;
import org.test4j.tools.datagen.AbastractDataGenerator;
import org.test4j.tools.datagen.RepeatDataGenerator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
@Test(groups = { "jtester", "tools" })
public class RepeatDataGeneratorTest extends JTester {

	@Test(dataProvider = "dataRepeat")
	public void testRepeatDataGenerator(int index, Object expected) {
		AbastractDataGenerator generator = RepeatDataGenerator.repeat("a", "b", "c", "d");
		Object o = generator.generate(index);
		want.object(o).isEqualTo(expected);
	}

	@DataProvider
	public Iterator dataRepeat() {
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
