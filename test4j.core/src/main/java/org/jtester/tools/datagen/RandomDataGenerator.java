package org.jtester.tools.datagen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("rawtypes")
public class RandomDataGenerator extends AbastractDataGenerator {
	private static Random random = new Random(System.currentTimeMillis());

	private final Class type;

	public RandomDataGenerator(Class type) {
		this.type = type;
	}

	@Override
	public Object generate(int index) {
		if (type == Integer.class || type == int.class) {
			return random.nextInt();
		}
		if (type == Long.class || type == long.class) {
			return random.nextLong();
		}
		if (type == Short.class || type == short.class) {
			String value = String.valueOf(random.nextInt());
			return Short.valueOf(value);
		}
		if (type == Double.class || type == double.class) {
			return random.nextDouble();
		}
		if (type == Float.class || type == float.class) {
			return random.nextFloat();
		}
		if (type == Boolean.class || type == boolean.class) {
			return random.nextBoolean();
		}
		if (type == BigDecimal.class) {
			return BigDecimal.valueOf(random.nextDouble());
		}
		if (type == BigInteger.class) {
			return BigDecimal.valueOf(random.nextLong());
		}
		if (type == String.class) {
			return UUID.randomUUID().toString();
		}

		throw new RuntimeException(
				"unsupport this type random generate, only support type[Number, String, BigInteger, BigDecimal].");
	}
}
