package org.jtester.datafilling.filler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.FillList;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.common.FillingConstants;
import org.jtester.datafilling.strategy.AttributeStrategy;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.datafilling.strategy.EmptyStrategy;
import org.jtester.module.core.utility.MessageHelper;

@SuppressWarnings("rawtypes")
public class ArrayFiller extends Filler {
	public ArrayFiller(DataFactory strategy, Map<String, Type> argsTypeMap) {
		super(strategy, argsTypeMap);
	}

	private FillList getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillList.class.isAssignableFrom(annotation.getClass())) {
				FillList filling = (FillList) annotation;
				return filling;
			}
		}
		return null;
	}

	/**
	 * It returns an Array with the first element set
	 * 
	 * 
	 * @param attribute
	 *            The array type
	 * @return Array with the first element set
	 * @throws Exception
	 */
	public Object fillingArray(AttributeInfo attribute) throws Exception {
		int nbrElements = FillingConstants.ARRAY_DEFAULT_SIZE;
		FillList filling = this.getFilling(attribute);
		AttributeStrategy elementStrategy = null;

		if (null != filling) {
			nbrElements = filling.size();
			elementStrategy = filling.collectionElementStrategy().newInstance();
		}

		return createArrayInstance(attribute, nbrElements, filling, elementStrategy);
	}

	private Object createArrayInstance(AttributeInfo attribute, int nbrElements, FillList filling,
			AttributeStrategy elementStrategy) throws Exception {
		AttributeInfo itemAttribute = getArrayItemAttribute(attribute);
		itemAttribute.setAttrAnnotations(attribute.getAttrAnnotations());
		Object array = Array.newInstance(itemAttribute.getAttrClaz(), nbrElements);
		for (int i = 0; i < nbrElements; i++) {
			Object arrayElement = createArrayItem(itemAttribute, filling, elementStrategy);
			Array.set(array, i, arrayElement);
		}
		return array;
	}

	/**
	 * 获取数组项的具体属性类型
	 * 
	 * @param arrayAttribute
	 * @param typeArgsMap
	 * @return
	 */
	private AttributeInfo getArrayItemAttribute(AttributeInfo arrayAttribute) {
		Class itemClaz = arrayAttribute.getComponentType();
		AttributeInfo ainfo = new AttributeInfo(itemClaz);

		Type[] genericTypeArgs = new Type[] {};
		try {
			final Type genericType = arrayAttribute.getDeclaredField().getGenericType();
			if (!(genericType instanceof GenericArrayType)) {
				return ainfo;
			}
			final Type type = ((GenericArrayType) genericType).getGenericComponentType();
			if (!(type instanceof TypeVariable)) {
				return ainfo;
			}
			final Type typeVarType = argsTypeMap.get(((TypeVariable) type).getName());
			if (typeVarType instanceof ParameterizedType) {
				final ParameterizedType pType = (ParameterizedType) typeVarType;
				itemClaz = (Class) pType.getRawType();
				genericTypeArgs = pType.getActualTypeArguments();
			} else {
				itemClaz = (Class) typeVarType;
			}
		} catch (NoSuchFieldException e) {
			MessageHelper.info("Cannot get the declared field type for field " + arrayAttribute.getAttrName()
					+ " of class " + arrayAttribute.getPoJoName());
		}
		ainfo = new AttributeInfo(itemClaz).setAttrArgs(genericTypeArgs);
		return ainfo;
	}

	private Object createArrayItem(AttributeInfo componentType, FillList filling,
			AttributeStrategy elementStrategy) throws Exception {
		if (null != elementStrategy && EmptyStrategy.class.isAssignableFrom(filling.collectionElementStrategy())
				&& componentType.isObject()) {
			return elementStrategy.getValue();
		} else if (null != elementStrategy
				&& !EmptyStrategy.class.isAssignableFrom(filling.collectionElementStrategy())) {
			return returnAttributeDataStrategyValue(componentType.getAttrClaz(), elementStrategy);
		} else {
			Object arrayElement = filling(this.strategy, componentType);
			return arrayElement;
		}
	}
}
