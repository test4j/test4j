package org.jtester.datafilling.filler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.FillList;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.common.FillingConstants;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.strategy.AttributeStrategy;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.datafilling.strategy.EmptyStrategy;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapFiller extends Filler {

	public MapFiller(DataFactory strategy, Map<String, Type> argsTypeMap) {
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
	 * It manufactures and returns a Map with at least one element in it It
	 * fills a Map with the required number of elements of the required type.
	 * 
	 * <p>
	 * This method has a so-called side-effect. It updates the Map given as
	 * argument.
	 * </p>
	 * 
	 * @param attribute
	 *            The type of the POJO map attribute
	 * 
	 * @throws PoJoFillException
	 *             If an error occurred while creating the Map object
	 */
	public Map fillingMap(AttributeInfo attribute) {
		FillList filling = this.getFilling(attribute);
		try {
			AttributeInfo keyAttribute = attribute.getArgTypeAttribute(0, this.argsTypeMap).setAttrAnnotations(
					attribute.getAttrAnnotations());
			AttributeInfo valueAttribute = attribute.getArgTypeAttribute(1, this.argsTypeMap).setAttrAnnotations(
					attribute.getAttrAnnotations());

			Map mapInstance = getAttributeMapInstance(attribute);
			AttributeStrategy keyStrategy = null;
			int nbrElements = FillingConstants.ARRAY_DEFAULT_SIZE;
			AttributeStrategy<?> elementStrategy = null;
			if (null != filling) {
				nbrElements = filling.size();
				keyStrategy = filling.mapKeyStrategy().newInstance();
				elementStrategy = filling.mapElementStrategy().newInstance();
			}

			for (int i = 0; i < nbrElements; i++) {
				Object keyValue = getMapKeyOrElementValue(keyStrategy, keyAttribute);
				Object elementValue = getMapKeyOrElementValue(elementStrategy, valueAttribute);
				mapInstance.put(keyValue, elementValue);
			}
			return mapInstance;
		} catch (Exception e) {
			throw new PoJoFillException("An exception occurred while creating a Map object", e);
		}
	}

	/**
	 * 根据Pojo属性attribute返回一个具体的map实例
	 * 
	 * @return
	 */
	private Map getAttributeMapInstance(AttributeInfo attribute) {
		try {
			Map retValue = attribute.getAttributeValue();
			if (retValue == null) {
				throw new RuntimeException("create map instance use atribute error!");
			} else {
				return retValue;
			}
		} catch (Exception e) {
			return createDefaultMap(attribute.getAttrClaz());
		}
	}

	/**
	 * It fills a Map key or value with the appropriate value, considering
	 * attribute-level customisation.
	 * 
	 * @param strategy
	 *            The strategy to use to fill the Map key or value element
	 * 
	 * @param keyOrValueType
	 *            The Map key / element type
	 * 
	 * @return A Map key or value
	 * @throws Exception
	 */
	private Object getMapKeyOrElementValue(AttributeStrategy strategy, AttributeInfo keyOrValueType) throws Exception {
		Object retValue = null;
		if (null != strategy && EmptyStrategy.class.isAssignableFrom(strategy.getClass())
				&& Object.class.equals(keyOrValueType.getAttrClaz())) {
			retValue = strategy.getValue();
		} else if (null != strategy && !EmptyStrategy.class.isAssignableFrom(strategy.getClass())) {
			retValue = returnAttributeDataStrategyValue(keyOrValueType.getAttrClaz(), strategy);
		} else {
			retValue = fillingAttribute(keyOrValueType);
		}
		return retValue;
	}

	/**
	 * It manufactures and returns a default instance for each map type<br>
	 * 返回一个默认的map实例
	 * 
	 * <p>
	 * The default implementation for a {@link ConcurrentMap} is
	 * {@link ConcurrentHashMap}
	 * </p>
	 * <p>
	 * The default implementation for a {@link SortedMap} is a {@link TreeMap}
	 * </p>
	 * <p>
	 * The default Map is none of the above was recognised is a {@link HashMap}
	 * </p>
	 * 
	 * @param attributeType
	 *            The attribute type
	 * @return A default instance for each map type
	 * 
	 */
	public static Map createDefaultMap(Class attributeType) {
		Map mapInstance = null;
		if (SortedMap.class.isAssignableFrom(attributeType)) {
			mapInstance = new TreeMap();
		} else if (ConcurrentMap.class.isAssignableFrom(attributeType)) {
			mapInstance = new ConcurrentHashMap();
		} else {
			mapInstance = new HashMap();
		}
		return mapInstance;
	}
}
