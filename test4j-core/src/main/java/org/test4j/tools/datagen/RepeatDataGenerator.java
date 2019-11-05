package org.test4j.tools.datagen;

public class RepeatDataGenerator extends AbstractDataGenerator {
	private final Object[] datas;

	private final int size;

	public RepeatDataGenerator(Object[] datas) {
		this.datas = datas;
		if (datas == null || datas.length == 0) {
			throw new RuntimeException("the repeat datas can't be empty.");
		}
		this.size = datas.length;
	}

	@Override
	public Object generate(int index) {
		int pos = index % size;
		return datas[pos];
	}
}
