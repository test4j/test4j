package org.jtester.datafilling;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.jtester.datafilling.annotations.FillWith;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.common.FillerHelper;
import org.jtester.datafilling.common.FillingConstants;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.filler.ArrayFiller;
import org.jtester.datafilling.filler.CollectionFiller;
import org.jtester.datafilling.filler.EnumFiller;
import org.jtester.datafilling.filler.MapFiller;
import org.jtester.datafilling.filler.PoJoFiller;
import org.jtester.datafilling.filler.PrimitiveFiller;
import org.jtester.datafilling.filler.StringFiller;
import org.jtester.datafilling.strategy.AttributeStrategy;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.datafilling.strategy.RandomDataFactory;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.tools.commons.ClazzHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Filler {
	protected DataFactory strategy;

	protected Map<String, Type> argsTypeMap;

	public Filler(DataFactory strategy, Map<String, Type> argsTypeMap) {
		this.strategy = strategy;
		this.argsTypeMap = argsTypeMap;
	}

	/**
	 * It retrieves the value for the {@link FillWith} annotation with which the
	 * attribute was annotated
	 * 
	 * @param attributeType
	 *            The attribute type, used for type checking
	 * @param attributeStrategy
	 *            The {@link AttributeStrategy} to use
	 * @return The value for the {@link FillWith} annotation with which the
	 *         attribute was annotated
	 * @throws Exception
	 */
	public Object returnAttributeDataStrategyValue(Class attributeType, AttributeStrategy attributeStrategy)
			throws Exception {
		try {
			Method attributeStrategyMethod = attributeStrategy.getClass().getMethod(
					FillingConstants.PoJoGen_ATTRIBUTE_STRATEGY_METHOD_NAME, new Class[] {});

			if (!attributeType.isAssignableFrom(attributeStrategyMethod.getReturnType())) {
				String errMsg = "The type of the Attribute Strategy is not " + attributeType.getName() + " but "
						+ attributeStrategyMethod.getReturnType().getName() + ". An exception will be thrown.";
				MessageHelper.error(errMsg);
				throw new IllegalArgumentException(errMsg);
			}

			return attributeStrategy.getValue();

		} catch (SecurityException e) {
			throw new IllegalStateException(
					"A security issue occurred while retrieving the Attribute Strategy details", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("It seems the Attribute Annotation is of the wrong type", e);
		}
	}

	/**
	 * It manufactures and returns the value for a POJO attribute.
	 * 
	 * @param attribute
	 *            The type of the attribute for which a value is being
	 *            manufactured
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("<T, V>" for
	 *            example) with their actual types
	 * @return The value for an attribute
	 * 
	 * @throws Exception
	 */
	public <T> T fillingAttribute(AttributeInfo attribute) throws Exception {
		if (attribute.isPrimitive()) {
			return new PrimitiveFiller(strategy).fillingPrimitive(attribute);
		} else if (attribute.isString()) {
			return (T) new StringFiller(strategy).fillingString(attribute);
		} else if (attribute.isArray()) {
			return (T) new ArrayFiller(strategy, argsTypeMap).fillingArray(attribute);
		} else if (attribute.isCollection()) {
			return (T) new CollectionFiller(strategy, argsTypeMap).fillingCollection(attribute);
		} else if (attribute.isMap()) {
			return (T) new MapFiller(strategy, argsTypeMap).fillingMap(attribute);
		} else if (attribute.isEnum()) {
			return (T) new EnumFiller(strategy).fillingEnum(attribute);
		} else {
			return new PoJoFiller(strategy, argsTypeMap).fillingPoJo(attribute, 0);
		}
	}

	public static <T> T filling(Class clazz, Type... genericTypeArgs) {
		Object o = filling(RandomDataFactory.getInstance(), clazz, genericTypeArgs);
		return (T) o;
	}

	public static <T> T filling(DataFactory strategy, FillUp fillUp) {
		Class clazz = FillerHelper.getFillUpType(fillUp);
		Type[] argsType = fillUp.getArgTypes();
		Object o = filling(RandomDataFactory.getInstance(), clazz, argsType);
		return (T) o;
	}

	public static <T> T filling(FillUp fillUp) {
		Object o = filling(RandomDataFactory.getInstance(), fillUp);
		return (T) o;
	}

	public static <T> T filling(DataFactory strategy, Class clazz, Type... genericTypeArgs) {
		if (clazz.isInterface() || ClazzHelper.isAbstract(clazz)) {
			MessageHelper.warn("Cannot instantiate an interface or abstract class. Returning null.");
			return null;
		}
		AttributeInfo attribute = new AttributeInfo(clazz).setAttrArgs(genericTypeArgs);
		Object o = filling(strategy, attribute);
		return (T) o;
	}

	public static <T> T filling(DataFactory strategy, AttributeInfo attribute) {
		try {
			Object o = new Filler(strategy, new HashMap<String, Type>()).fillingAttribute(attribute);
			return (T) o;
		} catch (Exception e) {
			throw new PoJoFillException(e.getMessage(), e);
		}
	}
}
