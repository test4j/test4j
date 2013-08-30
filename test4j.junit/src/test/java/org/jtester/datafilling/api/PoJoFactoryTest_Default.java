package org.jtester.datafilling.api;

import java.lang.reflect.ParameterizedType;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.model.dto.GenericAttributePojo;
import org.jtester.datafilling.model.dto.GenericPojo;
import org.jtester.datafilling.model.dto.MultiDimensionalConstructorPojo;
import org.jtester.datafilling.model.dto.MultiDimensionalTestPojo;
import org.jtester.datafilling.strategies.PoJoParameterizedType;
import org.junit.Test;

public class PoJoFactoryTest_Default {

	@Test
	public void testPojoWithGenericFields() {
		final GenericAttributePojo pojo = Filler.filling(GenericAttributePojo.class);

		final GenericPojo<String, Long> genericPojo = pojo.getGenericPojo();
		Assert.assertNotNull("The GenericPojo object cannot be null!", genericPojo);

		Assert.assertNotNull("The generated object cannot be null!", genericPojo.getFirstValue());
		Assert.assertEquals("The generated object must be a String!", String.class, genericPojo.getFirstValue()
				.getClass());
		Assert.assertNotNull("The generated object cannot be null!", genericPojo.getSecondValue());
		Assert.assertEquals("The generated object must be a Long!", Long.class, genericPojo.getSecondValue().getClass());
		Assert.assertNotNull("The generated list cannot be null!", genericPojo.getFirstList());
		Assert.assertEquals("The generated list type must be of String!", String.class,
				genericPojo.getFirstList().get(0).getClass());
		Assert.assertNotNull("The generated array cannot be null!", genericPojo.getSecondArray());
		Assert.assertEquals("The generated array type must be of Long!", Long.class,
				genericPojo.getSecondArray()[0].getClass());
		Assert.assertNotNull("The generated map cannot be null!", genericPojo.getFirstSecondMap());
		Assert.assertEquals("The generated map key type must be of String!", String.class, genericPojo
				.getFirstSecondMap().entrySet().iterator().next().getKey().getClass());
		Assert.assertEquals("The generated map value type must be of Long!", Long.class, genericPojo
				.getFirstSecondMap().entrySet().iterator().next().getValue().getClass());

	}

	@Test
	public void testGenericPojoManufacture() {
		final GenericPojo<Double, Boolean> pojo = Filler.filling(GenericPojo.class, Double.class, Boolean.class);

		Assert.assertNotNull("The GenericPojo object cannot be null!", pojo);

		Assert.assertNotNull("The generated object cannot be null!", pojo.getFirstValue());
		Assert.assertEquals("The generated object must be a Double!", Double.class, pojo.getFirstValue().getClass());
		Assert.assertNotNull("The generated object cannot be null!", pojo.getSecondValue());
		Assert.assertEquals("The generated object must be a Boolean!", Boolean.class, pojo.getSecondValue().getClass());
		Assert.assertNotNull("The generated list cannot be null!", pojo.getFirstList());
		Assert.assertEquals("The generated list type must be of Double!", Double.class, pojo.getFirstList().get(0)
				.getClass());
		Assert.assertNotNull("The generated array cannot be null!", pojo.getSecondArray());
		Assert.assertEquals("The generated array type must be of Boolean!", Boolean.class,
				pojo.getSecondArray()[0].getClass());
		Assert.assertNotNull("The generated map cannot be null!", pojo.getFirstSecondMap());
		Assert.assertEquals("The generated map key type must be of Double!", Double.class, pojo.getFirstSecondMap()
				.entrySet().iterator().next().getKey().getClass());
		Assert.assertEquals("The generated map value type must be of Boolean!", Boolean.class, pojo.getFirstSecondMap()
				.entrySet().iterator().next().getValue().getClass());

	}

	@Test
	public void testMultiDimensionalTestPojo() {
		final MultiDimensionalTestPojo pojo = Filler.filling(MultiDimensionalTestPojo.class);

		checkMultiDimensionalPojo(pojo);
	}

	@Test
	public void testConstructorMultiDimensionalPojo() {
		final MultiDimensionalConstructorPojo pojo = Filler.filling(MultiDimensionalConstructorPojo.class);

		checkMultiDimensionalPojo(pojo);
	}

	@Test
	public void testMultiDimensionalPojoManufacture() {
		ParameterizedType twoDimensionalStringListType = new PoJoParameterizedType(List.class,
				new PoJoParameterizedType(List.class, String.class));
		ParameterizedType longDoubleMapType = new PoJoParameterizedType(Map.class, Long.class, Double.class);

		final GenericPojo<List<List<String>>, Map<Long, Double>> pojo = Filler.filling(GenericPojo.class,
				twoDimensionalStringListType, longDoubleMapType);

		Assert.assertNotNull("The GenericPojo object cannot be null!", pojo);

		Assert.assertNotNull("The generated object cannot be null!", pojo.getFirstValue());
		Assert.assertEquals("The generated object must be a String!", String.class, pojo.getFirstValue().get(0).get(0)
				.getClass());
		Assert.assertNotNull("The generated object cannot be null!", pojo.getSecondValue());
		Assert.assertEquals("The generated object must be a Long!", Long.class, pojo.getSecondValue().keySet()
				.iterator().next().getClass());
		Assert.assertEquals("The generated object must be a Double!", Double.class, pojo.getSecondValue().values()
				.iterator().next().getClass());
		Assert.assertNotNull("The generated list cannot be null!", pojo.getFirstList());
		Assert.assertEquals("The generated list type must be of String!", String.class,
				pojo.getFirstList().get(0).get(0).get(0).getClass());
		Assert.assertNotNull("The generated array cannot be null!", pojo.getSecondArray());
		Assert.assertEquals("The generated array type must be of Long!", Long.class, pojo.getSecondArray()[0].keySet()
				.iterator().next().getClass());
		Assert.assertEquals("The generated array type must be of Double!", Double.class, pojo.getSecondArray()[0]
				.values().iterator().next().getClass());
		Assert.assertNotNull("The generated map cannot be null!", pojo.getFirstSecondMap());
		Assert.assertEquals("The generated map key type must be of String!", String.class, pojo.getFirstSecondMap()
				.entrySet().iterator().next().getKey().get(0).get(0).getClass());
		Assert.assertEquals("The generated map value type must be of Long!", Long.class, pojo.getFirstSecondMap()
				.entrySet().iterator().next().getValue().keySet().iterator().next().getClass());
		Assert.assertEquals("The generated map value type must be of Double!", Double.class, pojo.getFirstSecondMap()
				.entrySet().iterator().next().getValue().values().iterator().next().getClass());
	}

	private void checkMultiDimensionalPojo(final MultiDimensionalTestPojo pojo) {
		Assert.assertNotNull("The GenericPojo object cannot be null!", pojo);

		checkMultiDimensionalCollection(pojo.getThreeDimensionalList(), String.class);
		checkMultiDimensionalCollection(pojo.getThreeDimensionalQueue(), Date.class);
		checkMultiDimensionalCollection(pojo.getThreeDimensionalSet(), Double.class);
		checkMultiDimensionalCollection(pojo.getThreeDimensionalCollection(), Long.class);

		Assert.assertEquals("The generated Array must have size=2!", 2, pojo.getThreeDimensionalArray().length);
		Assert.assertEquals("The generated Array must have size=2!", 2, pojo.getThreeDimensionalArray()[0].length);
		Assert.assertEquals("The generated Array must have size=2!", 2, pojo.getThreeDimensionalArray()[0][0].length);
		Assert.assertEquals("The generated Array must be of String!", String.class,
				pojo.getThreeDimensionalArray()[0][0][0].getClass());

		Assert.assertEquals("The generated Map must have size=1!", 1, pojo.getThreeDimensionalMap().size());
		Entry<Boolean, Map<Float, Map<Integer, Calendar>>> entry = pojo.getThreeDimensionalMap().entrySet().iterator()
				.next();
		Assert.assertEquals("The generated Map entry key must be of Boolean!", Boolean.class, entry.getKey().getClass());
		Assert.assertEquals("The generated Map must have size=2!", 2, entry.getValue().size());
		Entry<Float, Map<Integer, Calendar>> entry2 = entry.getValue().entrySet().iterator().next();
		Assert.assertEquals("The generated Map entry key must be of Float!", Float.class, entry2.getKey().getClass());
		Assert.assertEquals("The generated Map must have size=2!", 2, entry2.getValue().size());
		Entry<Integer, Calendar> entry3 = entry2.getValue().entrySet().iterator().next();
		Assert.assertEquals("The generated Map entry key must be of Integer!", Integer.class, entry3.getKey()
				.getClass());
		Assert.assertEquals("The generated Map entry key must be of Calendar!", GregorianCalendar.class, entry3
				.getValue().getClass());
	}

	@SuppressWarnings("unchecked")
	private <T> void checkMultiDimensionalCollection(final Collection<?> collection, Class<T> type) {
		Assert.assertEquals("The generated List must have size=2!", 2, collection.size());
		Collection<?> subcollection = (Collection<?>) collection.iterator().next();
		Assert.assertEquals("The generated List must have size=2!", 2, subcollection.size());
		subcollection = (Collection<?>) subcollection.iterator().next();
		Assert.assertEquals("The generated List must have size=2!", 2, subcollection.size());
		T element = (T) subcollection.iterator().next();
		Assert.assertEquals("The generated List must be of " + type + "!", type, element.getClass());
	}
}
