package org.jtester.datafilling.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jtester.datafilling.FillUp;

@SuppressWarnings("rawtypes")
public class FillerHelper {
	public static Class getFillUpType(FillUp fillUp) {
		Class currentClass = fillUp.getClass();
		do {
			Type superclass = currentClass.getGenericSuperclass();

			if (superclass == FillUp.class) {
				throw new IllegalArgumentException("No type to be filled");
			}
			if (superclass == Object.class) {
				throw new IllegalArgumentException("You should new an internal class, new FillUp(){...};!");
			}
			if (!(superclass instanceof ParameterizedType)) {
				currentClass = (Class) superclass;
				continue;
			}
			Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];

			if (type instanceof Class) {
				return (Class) type;
			} else if (type instanceof ParameterizedType) {
				return (Class) ((ParameterizedType) type).getRawType();
			} else {
				throw new RuntimeException("to fill up type can't be a VariableType!");
			}

		} while (true);
	}
}
