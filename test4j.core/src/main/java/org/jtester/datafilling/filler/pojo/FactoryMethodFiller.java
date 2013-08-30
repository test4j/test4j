package org.jtester.datafilling.filler.pojo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.filler.PoJoFiller;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.module.core.utility.MessageHelper;

public class FactoryMethodFiller extends PoJoFiller {

	public FactoryMethodFiller(DataFactory strategy, Map<String, Type> argsTypeMap) {
		super(strategy, argsTypeMap);
	}

	/**
	 * If no publicly accessible constructors are available, the best we can do
	 * is to find a constructor (e.g. getInstance())<br>
	 * A candidate factory method is a method which returns the Class type
	 * 
	 * @param clazz
	 * @param typeArgsMap
	 * @param genericTypeArgs
	 * @return
	 * @throws Exception
	 */
	public Object newInstanceUseFactoryMethod(AttributeInfo attrinbute) throws Exception {
		List<Method> factoryMethods = attrinbute.getFactoryMethods();
		for (Method method : factoryMethods) {
			Object[] parameterValues = getMethodParaValues(attrinbute, method);
			try {
				MessageHelper.info("Could create an instance using " + method);
				return method.invoke(attrinbute.getAttrClaz(), parameterValues);
			} catch (Throwable t) {
				MessageHelper.warn("Could not create an instance for constructor: " + method
						+ ". Will try another one...");
			}
		}
		return null;
	}

	private Object[] getMethodParaValues(AttributeInfo attrinbute, Method method) throws Exception {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Type[] parameterTypes = method.getGenericParameterTypes();
		Object[] parameterValues = new Object[method.getParameterTypes().length];

		int index = 0;
		for (Type paramType : parameterTypes) {
			List<Annotation> annotations = Arrays.asList(parameterAnnotations[index]);
			AttributeInfo paraAttribute = AttributeInfo.exactArgAttributeInfo(paramType, argsTypeMap);
			paraAttribute.setAttrAnnotations(annotations);
			Object paraInstance = new Filler(this.strategy, argsTypeMap).fillingAttribute(paraAttribute);
			parameterValues[index] = paraInstance;
			index++;
		}
		return parameterValues;
	}
}
