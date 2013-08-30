package org.jtester.tools.datagen;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IncreaseDataGenerator extends AbastractDataGenerator {
	private Number from;
	private Number step;

	public IncreaseDataGenerator(Number from, Number step) {
		this.from = from;
		this.step = step;
	}

	@Override
	public Object generate(int index) {
		if (from instanceof Integer) {
			Integer value = from.intValue() + step.intValue() * index;
			return value;
		}
		if (from instanceof Long) {
			Long value = from.longValue() + step.longValue() * index;
			return value;
		}
		if (from instanceof Short) {
			Integer value = from.shortValue() + step.shortValue() * index;
			return Short.valueOf(String.valueOf(value));
		}
		if (from instanceof BigInteger) {
			Long value = from.longValue() + step.longValue() * index;
			return BigInteger.valueOf(value);
		}
		if (from instanceof Double) {
			Double value = from.doubleValue() + step.doubleValue() * index;
			return value;
		}
		if (from instanceof Float) {
			Float value = from.floatValue() + step.floatValue() * index;
			return value;
		}
		if (from instanceof BigDecimal) {
			Double value = from.doubleValue() + step.doubleValue() * index;
			return BigDecimal.valueOf(value);
		}

		throw new RuntimeException(
				"unsupport this type number increase, only support type[int, long, short, double, float, BigInteger, BigDecimal].");
	}
}
