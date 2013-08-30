package org.jtester.datafilling.filler.pojo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.FillWith;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.common.ClassFieldInfo;
import org.jtester.datafilling.common.FillingConstants;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.filler.PoJoFiller;
import org.jtester.datafilling.strategy.AttributeStrategy;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.tools.reflector.MethodAccessor;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SetterMethodFiller extends PoJoFiller {

	public SetterMethodFiller(DataFactory strategy, Map<String, Type> argsTypeMap) {
		super(strategy, argsTypeMap);
	}

	public <T> T fillingWithSetter(AttributeInfo pojoAttr, int depth) throws PoJoFillException {
		try {
			ClassFieldInfo classInfo = pojoAttr.getClassInfo();
			Object instance = pojoAttr.newInstance();
			for (Method setter : classInfo.getClassSetters()) {
				Object o = fillPoJoWithSetter(pojoAttr, depth, argsTypeMap, setter);
				new MethodAccessor(setter).invoke(instance, new Object[] { o });
			}
			return (T) instance;
		} catch (Exception e) {
			throw new PoJoFillException("An filling exception occurred:" + e.getMessage(), e);
		}
	}

	private FillWith getFilling(AttributeInfo fieldAttr) {
		for (Annotation annotation : fieldAttr.getAttrAnnotations()) {
			if (FillWith.class.isAssignableFrom(annotation.getClass())) {
				FillWith filling = (FillWith) annotation;
				return filling;
			}
		}
		return null;
	}

	/**
	 * 通过setter方法来填充PoJo实例
	 * 
	 * @param pojoInstance
	 *            pojo实例
	 * @param depth
	 *            当前填充深度
	 * @param typeArgsMap
	 *            泛型名称的类型对
	 * @param setter
	 *            set方法
	 * @throws Exception
	 */
	private Object fillPoJoWithSetter(AttributeInfo pojoAttr, int depth, final Map<String, Type> typeArgsMap,
			Method setter) throws Exception {
		Class[] parameterTypes = setter.getParameterTypes();
		if (parameterTypes.length != 1) {
			throw new IllegalStateException("A JavaBean setter should have only one argument");
		}

		AttributeInfo fieldAttr = getFieldAttributeInfo(pojoAttr, typeArgsMap, setter);
		FillWith attributeStrategyAnnotation = getFilling(fieldAttr);
		if (null != attributeStrategyAnnotation) {
			AttributeStrategy attributeStrategy = attributeStrategyAnnotation.value().newInstance();
			return returnAttributeDataStrategyValue(fieldAttr.getAttrClaz(), attributeStrategy);
		} else if (fieldAttr.isNestedAttribute()) {
			return getNestedObjectInstance(pojoAttr, depth, typeArgsMap);
		} else {
			Object setterArg = new Filler(strategy, typeArgsMap).fillingAttribute(fieldAttr);
			return setterArg;
		}
	}

	private AttributeInfo getFieldAttributeInfo(AttributeInfo pojoAttr, final Map<String, Type> typeArgsMap,
			Method setter) {
		Type attributeType = setter.getGenericParameterTypes()[0];
		String attributeName = ClassFieldInfo.extractFieldNameFromSetterMethod(setter);
		List<Annotation> pojoAttributeAnnotations = retrieveFieldAnnotations(pojoAttr, setter);
		AttributeInfo fieldAttr = AttributeInfo.exactArgAttributeInfo(attributeType, typeArgsMap);
		fieldAttr.setPojoClaz(pojoAttr.getAttrClaz()).setAttrName(attributeName);
		fieldAttr.setAttrAnnotations(pojoAttributeAnnotations);
		return fieldAttr;
	}

	/**
	 * Given the original class and the setter method, it returns all
	 * annotations for the field or an empty collection if no custom annotations
	 * were found on the field
	 * 
	 * @param clazz
	 *            The class containing the annotated attribute
	 * @param setter
	 *            The setter method
	 * @return all annotations for the field
	 */
	private List<Annotation> retrieveFieldAnnotations(AttributeInfo pojoAttr, Method setter) {
		List<Annotation> retValue = new ArrayList<Annotation>();
		String attributeName = ClassFieldInfo.extractFieldNameFromSetterMethod(setter);

		Field setterField = null;
		Class clazz = pojoAttr.getAttrClaz();
		while (clazz != null) {
			try {
				setterField = clazz.getDeclaredField(attributeName);
				break;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (SecurityException e) {
				throw e;
			}
		}

		if (setterField != null) {
			Annotation[] annotations = setterField.getAnnotations();
			if (annotations != null && annotations.length != 0) {
				retValue = Arrays.asList(annotations);
			}
		}
		return retValue;
	}

	/**
	 * 创建循环引用对象
	 * 
	 * @param depth
	 * @param typeArgsMap
	 * @param attributeType
	 * @return
	 * @throws Exception
	 */
	private Object getNestedObjectInstance(AttributeInfo pojoAttr, int depth, final Map<String, Type> typeArgsMap)
			throws Exception {
		if (depth < FillingConstants.MAX_DEPTH) {
			Object o = new PoJoFiller(this.strategy, typeArgsMap).fillingPoJo(pojoAttr, depth + 1);
			return o;
		} else {
			Object o = new PoJoFiller(strategy, typeArgsMap).fillingWithConstructorsOrFactory(pojoAttr);
			return o;
		}
	}
}
