package org.jtester.datafilling.strategies;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class PoJoParameterizedType implements ParameterizedType {

	private final Class<?> rawType;
	private final Type[] actualTypeArguments;

	public PoJoParameterizedType(final Class<?> rawType, final Type... actualTypeArguments) {
		super();
		this.rawType = rawType;
		this.actualTypeArguments = actualTypeArguments;
	}

	public Type[] getActualTypeArguments() {
		return actualTypeArguments;
	}

	public Class<?> getRawType() {
		return rawType;
	}

	public Type getOwnerType() {
		return null;
	}
}
