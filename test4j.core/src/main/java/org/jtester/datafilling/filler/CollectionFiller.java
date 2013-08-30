package org.jtester.datafilling.filler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.FillList;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.common.FillingConstants;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.strategy.AttributeStrategy;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.datafilling.strategy.EmptyStrategy;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollectionFiller extends Filler {

	public CollectionFiller(DataFactory strategy, Map<String, Type> argsTypeMap) {
		super(strategy, argsTypeMap);
	}

	public Collection fillingCollection(AttributeInfo attribute) {
		try {
			Collection collInstance = createCollectionInstance(attribute);
			AttributeInfo collItemAttribute = new AttributeInfo();
			if (!attribute.isEmptyArgs()) {
				collItemAttribute = AttributeInfo.exactArgAttributeInfo(attribute.getAttrArgs()[0], this.argsTypeMap);
			}
			fillCollection(collInstance, collItemAttribute.setAttrAnnotations(attribute.getAttrAnnotations()));
			return collInstance;
		} catch (Exception e) {
			throw new PoJoFillException("An exception occurred while resolving the collection", e);
		}

	}

	/**
	 * 根据PoJo属性创建Collection实例
	 * 
	 * @return
	 */
	private Collection createCollectionInstance(AttributeInfo attribute) {
		try {
			Collection collInstance = attribute.getAttributeValue();
			if (collInstance == null) {
				throw new RuntimeException("create collection used attribute error!");
			} else {
				return collInstance;
			}
		} catch (Exception e) {
			return createDefaultCollection(attribute.getAttrClaz());
		}
	}

	/**
	 * Given a collection type it returns an instance
	 * <p>
	 * <ul>
	 * <li>The default type for a {@link List} is an {@link ArrayList}</li>
	 * <li>The default type for a {@link Queue} is a {@link LinkedList}</li>
	 * <li>The default type for a {@link Set} is a {@link HashSet}</li>
	 * </ul>
	 * 
	 * </p>
	 * 
	 * @param collectionType
	 *            The collection type *
	 * @return an instance of the collection type
	 */
	public static Collection createDefaultCollection(Class collectionType) {
		if (List.class.isAssignableFrom(collectionType) || collectionType.equals(Collection.class)) {
			return new ArrayList();
		} else if (Queue.class.isAssignableFrom(collectionType)) {
			return new LinkedList();
		} else if (Set.class.isAssignableFrom(collectionType)) {
			return new HashSet();
		} else {
			throw new IllegalArgumentException("Collection type: " + collectionType + " not supported");
		}
	}

	private FillList getFilling(AttributeInfo attrinbute) {
		for (Annotation annotation : attrinbute.getAttrAnnotations()) {
			if (FillList.class.isAssignableFrom(annotation.getClass())) {
				FillList filling = (FillList) annotation;
				return filling;
			}
		}
		return null;
	}

	/**
	 * It fills a collection with the required number of elements of the
	 * required type.
	 * 
	 * <p>
	 * This method has a so-called side effect. It updates the collection passed
	 * as argument.
	 * </p>
	 * 
	 * @param collInstance
	 *            The Collection to be filled
	 * @param collItemAttribute
	 *            The type of the collection element and The generic type
	 *            arguments for the current generic class instance
	 * @throws Exception
	 * 
	 */
	public void fillCollection(Collection collInstance, AttributeInfo collItemAttribute) throws Exception {
		FillList collectionAnnotation = this.getFilling(collItemAttribute);

		AttributeStrategy elementStrategy = null;
		int nbrElements = FillingConstants.ARRAY_DEFAULT_SIZE;
		if (null != collectionAnnotation) {
			nbrElements = collectionAnnotation.size();
			elementStrategy = collectionAnnotation.collectionElementStrategy().newInstance();
		}

		for (int i = 0; i < nbrElements; i++) {
			Object item = getCollctionItem(collItemAttribute, elementStrategy);
			collInstance.add(item);
		}
	}

	private Object getCollctionItem(AttributeInfo collItemAttribute, AttributeStrategy elementStrategy)
			throws Exception {
		if (null != elementStrategy && EmptyStrategy.class.isAssignableFrom(elementStrategy.getClass())
				&& Object.class.equals(collItemAttribute.getAttrClaz())) {
			return elementStrategy.getValue();
		} else if (null != elementStrategy && !EmptyStrategy.class.isAssignableFrom(elementStrategy.getClass())) {
			return returnAttributeDataStrategyValue(collItemAttribute.getAttrClaz(), elementStrategy);
		} else {
			return fillingAttribute(collItemAttribute);
		}
	}
}
