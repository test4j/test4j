package org.jtester.datafilling;

import java.lang.reflect.Type;

//@SuppressWarnings({ "rawtypes", "unchecked" })
public class FillUp<T> {
	private final Type[] argTypes;

	/**
	 * 泛型参数
	 * 
	 * @param argTypes
	 */
	public FillUp(Type... argTypes) {
		this.argTypes = argTypes;
	}

	public Type[] getArgTypes() {
		return argTypes;
	}
}
