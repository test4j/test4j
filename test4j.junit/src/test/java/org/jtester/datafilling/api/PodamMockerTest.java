package org.jtester.datafilling.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import junit.framework.Assert;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.ByteValueWithErrorPojo;
import org.jtester.datafilling.annotations.CollectionAnnotationPojo;
import org.jtester.datafilling.annotations.StrategyPojo;
import org.jtester.datafilling.annotations.StringWithWrongStrategyTypePojo;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.model.AbstractTestPojo;
import org.jtester.datafilling.model.CollectionsPojo;
import org.jtester.datafilling.model.ConstructorWithSelfReferencesButNoDefaultConstructorPojo;
import org.jtester.datafilling.model.ConstructorWithSelfReferencesPojo;
import org.jtester.datafilling.model.EnumsPojo;
import org.jtester.datafilling.model.ExcludeAnnotationPojo;
import org.jtester.datafilling.model.ImmutableNoHierarchicalAnnotatedPojo;
import org.jtester.datafilling.model.ImmutableNonAnnotatedPojo;
import org.jtester.datafilling.model.ImmutableWithGenericCollectionsPojo;
import org.jtester.datafilling.model.ImmutableWithNonGenericCollectionsPojo;
import org.jtester.datafilling.model.InterfacePojo;
import org.jtester.datafilling.model.NoDefaultConstructorPojo;
import org.jtester.datafilling.model.NoSetterWithCollectionInConstructorPojo;
import org.jtester.datafilling.model.OneDimensionalChildPojo;
import org.jtester.datafilling.model.OneDimensionalTestPojo;
import org.jtester.datafilling.model.PrivateNoArgConstructorPojo;
import org.jtester.datafilling.model.RecursivePojo;
import org.jtester.datafilling.model.SimplePojoToTestSetters;
import org.jtester.datafilling.model.SingletonWithParametersInStaticFactoryPojo;
import org.jtester.datafilling.model.EnumsPojo.RatePodamInternal;
import org.jtester.datafilling.model.dto.NoDefaultPublicConstructorPojo;
import org.jtester.datafilling.model.dto.PrivateOnlyConstructorPojo;
import org.jtester.datafilling.model.dto.ProtectedNonDefaultConstructorPojo;
import org.jtester.datafilling.utils.ExternalRatePodamEnum;
import org.jtester.datafilling.utils.FillDataTestConstants;
import org.jtester.datafilling.utils.PodamTestUtils;
import org.jtester.module.ICore;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class PodamMockerTest implements ICore {

	@Test
	public void testMockerForClassWithoutDefaultConstructor() {
		NoDefaultConstructorPojo pojo = Filler.filling(NoDefaultConstructorPojo.class);
		Assert.assertNotNull("The pojo with no default constructors must not be null!", pojo);
	}

	@Test
	public void testMockerForAbstractClass() {
		AbstractTestPojo pojo = Filler.filling(AbstractTestPojo.class);
		Assert.assertNull("The abstract pojo should be null!", pojo);
	}

	@Test
	public void testMockerForInterface() {
		InterfacePojo pojo = Filler.filling(InterfacePojo.class);
		Assert.assertNull("The interface pojo should be null!", pojo);

	}

	@Test
	public void testMockerForPojoWithPrivateNoArgConstructor() {
		PrivateNoArgConstructorPojo pojo = Filler.filling(PrivateNoArgConstructorPojo.class);
		Assert.assertNotNull("The pojo with private default constructor cannot be null!", pojo);
	}

	@Test
	public void testOneDimensionalTestPojo() {
		OneDimensionalTestPojo pojo = Filler.filling(OneDimensionalTestPojo.class);
		Assert.assertNotNull("The object cannot be null!", pojo);

		Boolean booleanObjectField = pojo.getBooleanObjectField();
		Assert.assertTrue("The boolean object field should have a value of TRUE", booleanObjectField);

		boolean booleanField = pojo.isBooleanField();
		Assert.assertTrue("The boolean field should have a value of TRUE", booleanField);

		byte byteField = pojo.getByteField();
		Assert.assertTrue("The byte field should not be zero", byteField != 0);

		Byte byteObjectField = pojo.getByteObjectField();
		Assert.assertTrue("The Byte object field should not be zero", byteObjectField != 0);

		short shortField = pojo.getShortField();
		Assert.assertTrue("The short field should not be zero", shortField != 0);

		Short shortObjectField = pojo.getShortObjectField();
		Assert.assertTrue("The Short Object field should not be zero", shortObjectField != 0);

		char charField = pojo.getCharField();
		Assert.assertTrue("The char field should not be zero", charField != 0);
		Character characterObjectField = pojo.getCharObjectField();
		Assert.assertTrue("The Character object field should not be zero", characterObjectField != 0);

		int intField = pojo.getIntField();
		Assert.assertTrue("The int field cannot be zero", intField != 0);
		Integer integerField = pojo.getIntObjectField();
		Assert.assertTrue("The Integer object field cannot be zero", integerField != 0);

		long longField = pojo.getLongField();
		Assert.assertTrue("The long field cannot be zero", longField != 0);
		Long longObjectField = pojo.getLongObjectField();
		Assert.assertTrue("The Long object field cannot be zero", longObjectField != 0);

		float floatField = pojo.getFloatField();
		Assert.assertTrue("The float field cannot be zero", floatField != 0.0);
		Float floatObjectField = pojo.getFloatObjectField();
		Assert.assertTrue("The Float object field cannot be zero", floatObjectField != 0.0);

		double doubleField = pojo.getDoubleField();
		Assert.assertTrue("The double field cannot be zero", doubleField != 0.0d);
		Double doubleObjectField = pojo.getDoubleObjectField();
		Assert.assertTrue("The Double object field cannot be zero", doubleObjectField != 0.0d);

		String stringField = pojo.getStringField();
		Assert.assertNotNull("The String field cannot be null", stringField);
		Assert.assertFalse("The String field cannot be empty", stringField.equals(""));

		Object objectField = pojo.getObjectField();
		Assert.assertNotNull("The Object field cannot be null", objectField);

		Calendar calendarField = pojo.getCalendarField();
		this.checkCalendarIsValid(calendarField);

		Date dateField = pojo.getDateField();
		Assert.assertNotNull("The date field is not valid", dateField);

		Random[] randomArray = pojo.getRandomArray();
		Assert.assertNotNull("The array of Random objects cannot be null!", randomArray);
		Assert.assertTrue("The array of Random length should be one!", randomArray.length == 1);
		Random random = randomArray[0];
		Assert.assertNotNull("The Random array element at [0] should not be null", random);

		int[] intArray = pojo.getIntArray();
		Assert.assertNotNull("The array of ints cannot be null!", intArray);
		Assert.assertTrue("The array of ints length should be one!", intArray.length == 1);
		Assert.assertTrue("The first element in the array of ints must be different from zero!", intArray[0] != 0);

		boolean[] booleanArray = pojo.getBooleanArray();
		Assert.assertNotNull("The array of booleans cannot be null!", booleanArray);
		Assert.assertTrue("The array of boolean length should be one!", booleanArray.length == 1);

		BigDecimal bigDecimalField = pojo.getBigDecimalField();
		Assert.assertNotNull("The BigDecimal field cannot be null!", bigDecimalField);

	}

	@Test
	public void testRecursiveHierarchyPojo() {

		RecursivePojo pojo = Filler.filling(RecursivePojo.class);
		Assert.assertNotNull("The recursive pojo cannot be null!", pojo);
		Assert.assertTrue("The integer value in the pojo should not be zero!", pojo.getIntField() != 0);

		RecursivePojo parentPojo = pojo.getParent();
		Assert.assertNotNull("The parent pojo cannot be null!", parentPojo);
		Assert.assertTrue("The integer value in the parent pojo should not be zero!", parentPojo.getIntField() != 0);
		Assert.assertNotNull("The parent attribute of the parent pojo cannot be null!", parentPojo.getParent());

	}

	@Test
	public void testImmutableNoHierarchicalAnnotatedPojo() {

		ImmutableNoHierarchicalAnnotatedPojo pojo = Filler.filling(ImmutableNoHierarchicalAnnotatedPojo.class);
		Assert.assertNotNull("The Immutable Simple Pojo cannot be null!", pojo);
		int intField = pojo.getIntField();
		Assert.assertTrue("The int field cannot be zero", intField != 0);
		Calendar dateCreated = pojo.getDateCreated();
		Assert.assertNotNull("The Date Created Calendar object cannot be null!", dateCreated);
		Assert.assertNotNull("The Date object within the dateCreated Calendar object cannot be null!",
				dateCreated.getTime());
		long[] longArray = pojo.getLongArray();
		Assert.assertNotNull("The array of longs cannot be null!", longArray);
		Assert.assertTrue("The array of longs cannot be empty!", longArray.length > 0);
		long longElement = longArray[0];
		Assert.assertTrue("The long element within the long array cannot be zero!", longElement != 0);

	}

	@Test
	public void testImmutableNonAnnotatedPojo() {

		ImmutableNonAnnotatedPojo pojo = Filler.filling(ImmutableNonAnnotatedPojo.class);
		Assert.assertNotNull("The immutable non annotated POJO should not be null!", pojo);

		Assert.assertNotNull("The date created cannot be null!", pojo.getDateCreated());

		Assert.assertTrue("The int field cannot be zero!", pojo.getIntField() != 0);

		long[] longArray = pojo.getLongArray();
		Assert.assertNotNull("The array of longs cannot be null!", longArray);
		Assert.assertTrue("The array of longs must have 1 element!", longArray.length == 1);

	}

	@Test
	public void testPojoWithSelfReferencesInConstructor() {
		ConstructorWithSelfReferencesPojo pojo = Filler.filling(ConstructorWithSelfReferencesPojo.class);
		Assert.assertNotNull("The POJO cannot be null!", pojo);
		Assert.assertNotNull("The first self-reference cannot be null!", pojo.getParent());
		Assert.assertNotNull("The second self-reference cannot be null!", pojo.getAnotherParent());
	}

	@Test
	public void testPojoWithSelfReferenceInConstructorButNoDefaultConstructor() {
		Object o = Filler.filling(ConstructorWithSelfReferencesButNoDefaultConstructorPojo.class);
		want.object(o).notNull();
	}

	@Test
	public void testPodamExcludeAnnotation() {
		ExcludeAnnotationPojo pojo = Filler.filling(ExcludeAnnotationPojo.class);
		Assert.assertNotNull("The pojo should not be null!", pojo);
		int intField = pojo.getIntField();
		Assert.assertTrue("The int field should not be zero!", intField != 0);
		Assert.assertNull("The other object in the pojo should be null because annotated with PodamExclude!",
				pojo.getSomePojo());
	}

	@Test
	public void testInheritance() {
		OneDimensionalChildPojo pojo = Filler.filling(OneDimensionalChildPojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);
		int parentIntField = pojo.getParentIntField();
		Assert.assertTrue("The super int field must be <= 10", parentIntField <= 10);
		Calendar parentCalendarField = pojo.getParentCalendarField();
		this.checkCalendarIsValid(parentCalendarField);
		int intField = pojo.getIntField();
		Assert.assertTrue("The int field must be different from zero!", intField != 0);
		String strField = pojo.getStrField();
		Assert.assertNotNull("The string field cannot be null!", strField);
		Assert.assertTrue("The String field cannot be empty", strField.length() != 0);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testCollectionsPojo() {

		CollectionsPojo pojo = Filler.filling(CollectionsPojo.class);
		Assert.assertNotNull("The POJO cannot be null!", pojo);
		List<String> strList = pojo.getStrList();
		this.validateReturnedList(strList);
		ArrayList<String> arrayListStr = pojo.getArrayListStr();
		this.validateReturnedList(arrayListStr);
		List<String> copyOnWriteList = pojo.getCopyOnWriteList();
		this.validateReturnedList(copyOnWriteList);
		HashSet<String> hashSetStr = pojo.getHashSetStr();
		this.validateReturnedSet(hashSetStr);
		List<String> listStrCollection = new ArrayList<String>(pojo.getStrCollection());
		this.validateReturnedList(listStrCollection);
		Set<String> setStrCollection = new HashSet<String>(pojo.getStrCollection());
		this.validateReturnedSet(setStrCollection);
		Set<String> strSet = pojo.getStrSet();
		this.validateReturnedSet(strSet);
		Map<String, OneDimensionalTestPojo> map = pojo.getMap();
		this.validateHashMap(map);
		HashMap<String, OneDimensionalTestPojo> hashMap = pojo.getHashMap();
		this.validateHashMap(hashMap);
		ConcurrentMap<String, OneDimensionalTestPojo> concurrentHashMap = pojo.getConcurrentHashMap();
		this.validateConcurrentHashMap(concurrentHashMap);
		ConcurrentHashMap<String, OneDimensionalTestPojo> concurrentHashMapImpl = pojo.getConcurrentHashMapImpl();
		this.validateConcurrentHashMap(concurrentHashMapImpl);
		Queue<SimplePojoToTestSetters> queue = pojo.getQueue();
		Assert.assertNotNull("The queue cannot be null!", queue);
		Assert.assertTrue("The queue must be an instance of LinkedList", queue instanceof LinkedList);
		SimplePojoToTestSetters pojoQueueElement = queue.poll();
		Assert.assertNotNull("The queue element cannot be null!", pojoQueueElement);

		List nonGenerifiedList = pojo.getNonGenerifiedList();
		Assert.assertNotNull("The non generified list cannot be null!", nonGenerifiedList);
		Assert.assertFalse("The non-generified list cannot be empty!", nonGenerifiedList.isEmpty());

		Map nonGenerifiedMap = pojo.getNonGenerifiedMap();
		Assert.assertNotNull("The non generified map cannot be null!", nonGenerifiedMap);
		Assert.assertFalse("The non generified Map cannot be empty!", nonGenerifiedMap.isEmpty());
		Object object = nonGenerifiedMap.get(nonGenerifiedMap.keySet().iterator().next());
		Assert.assertNotNull("The object element within the Map cannot be null!", object);

	}

	@Test
	public void testPojoWithNoSettersAndCollectionInConstructor() {

		NoSetterWithCollectionInConstructorPojo pojo = Filler
				.filling(NoSetterWithCollectionInConstructorPojo.class);
		Assert.assertNotNull("The POJO cannot be null!", pojo);
		List<String> strList = pojo.getStrList();
		Assert.assertNotNull("The collection of Strings in the constructor cannot be null!", strList);
		Assert.assertFalse("The collection of Strings in the constructor cannot be empty!", strList.isEmpty());
		String strElement = strList.get(0);
		Assert.assertNotNull("The collection element cannot be null!", strElement);

		int intField = pojo.getIntField();
		Assert.assertTrue("The int field in the constructor must be different from zero", intField != 0);

	}

	@Test(expected = PoJoFillException.class)
	public void testByteAnnotationWithNumberFormatError() {
		Filler.filling(ByteValueWithErrorPojo.class);
	}

	@Test
	public void testCollectionAnnotation() {
		CollectionAnnotationPojo pojo = Filler.filling(CollectionAnnotationPojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		List<String> strList = pojo.getStrList();
		Assert.assertNotNull("The string list cannot be null!", strList);
		Assert.assertFalse("The string list cannot be empty!", strList.isEmpty());
		Assert.assertTrue("The string list must have " + FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS
				+ " elements but it had only " + strList.size(),
				strList.size() == FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS);

		String[] strArray = pojo.getStrArray();
		Assert.assertNotNull("The array cannot be null!", strArray);
		Assert.assertFalse("The array cannot be empty!", strArray.length == 0);
		Assert.assertTrue("The number of elements in the array (" + strArray.length + ") does not match "
				+ FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS,
				strArray.length == FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS);

		Map<String, String> stringMap = pojo.getStringMap();
		Assert.assertNotNull("The map cannot be null!", stringMap);
		Assert.assertFalse("The map of strings cannot be empty!", stringMap.isEmpty());
		Assert.assertTrue("The number of elements in the map (" + stringMap.size() + ") does not match "
				+ FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS,
				stringMap.size() == FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS);

	}

	@Test
	public void testImmutablePojoWithNonGenericCollections1() {
		ImmutableWithNonGenericCollectionsPojo pojo = Filler.filling(ImmutableWithNonGenericCollectionsPojo.class);
		Collection<Object> nonGenerifiedCollection = pojo.getNonGenerifiedCollection();
		Assert.assertNotNull("The non-generified collection cannot be null!", nonGenerifiedCollection);
		Assert.assertFalse("The non-generified collection cannot be empty!", nonGenerifiedCollection.isEmpty());
		Assert.assertTrue("The number of elements in the collection: " + nonGenerifiedCollection.size()
				+ " does not match the expected value: " + ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS,
				nonGenerifiedCollection.size() == ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS);
	}

	@Test
	public void testImmutablePojoWithNonGenericCollections2() {
		ImmutableWithNonGenericCollectionsPojo pojo = Filler.filling(ImmutableWithNonGenericCollectionsPojo.class);
		Set<Object> nonGenerifiedSet = pojo.getNonGenerifiedSet();
		Assert.assertNotNull("The non-generified Set cannot be null!", nonGenerifiedSet);
		Assert.assertFalse("The non-generified Set cannot be empty!", nonGenerifiedSet.isEmpty());
		Assert.assertTrue("The number of elements in the Set: " + nonGenerifiedSet.size()
				+ " does not match the expected value: " + ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS,
				nonGenerifiedSet.size() == ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS);
	}

	@Test
	public void testImmutablePojoWithNonGenericCollections3() {
		ImmutableWithNonGenericCollectionsPojo pojo = Filler.filling(ImmutableWithNonGenericCollectionsPojo.class);
		Map<Object, Object> nonGenerifiedMap = pojo.getNonGenerifiedMap();
		Assert.assertNotNull("The non-generified map cannot be null!", nonGenerifiedMap);
		Assert.assertFalse("The non generified map cannot be empty!", nonGenerifiedMap.isEmpty());
		Assert.assertTrue("The number of elements in the map: " + nonGenerifiedMap.size()
				+ " does not match the expected value: " + ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS,
				nonGenerifiedMap.size() == ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS);

	}

	@Test
	public void testImmutablePojoWithGenerifiedCollectionsInConstructor() {

		ImmutableWithGenericCollectionsPojo pojo = Filler.filling(ImmutableWithGenericCollectionsPojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		Collection<OneDimensionalTestPojo> generifiedCollection = pojo.getGenerifiedCollection();
		Assert.assertNotNull("The generified collection cannot be null!", generifiedCollection);
		Assert.assertFalse("The generified collection cannot be empty!", generifiedCollection.isEmpty());
		Assert.assertTrue("The number of elements in the generified collection: " + generifiedCollection.size()
				+ " does not match the expected value: " + ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS,
				generifiedCollection.size() == ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS);

		Map<String, Calendar> generifiedMap = pojo.getGenerifiedMap();
		Assert.assertNotNull("The generified map cannot be null!", generifiedMap);
		Assert.assertFalse("The generified map cannot be empty!", generifiedMap.isEmpty());
		Assert.assertTrue("The number of elements in the generified map: " + generifiedMap.size()
				+ " does not match the expected value: " + ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS,
				generifiedMap.size() == ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS);

		Set<ImmutableWithNonGenericCollectionsPojo> generifiedSet = pojo.getGenerifiedSet();
		Assert.assertNotNull("The generified set cannot be null!", generifiedSet);
		Assert.assertFalse("The generified set cannot be empty!", generifiedSet.isEmpty());
		Assert.assertTrue("The number of elements in the generified set: " + generifiedSet.size()
				+ " does not match the expected value: " + ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS,
				generifiedSet.size() == ImmutableWithNonGenericCollectionsPojo.NBR_ELEMENTS);

	}

	@Test
	public void testSingletonWithParametersInPublicStaticMethod() {
		SingletonWithParametersInStaticFactoryPojo pojo = Filler
				.filling(SingletonWithParametersInStaticFactoryPojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		Assert.assertNotNull("The calendar object cannot be null!", pojo.getCreateDate());

		Assert.assertNotNull("The first name cannot be null!", pojo.getFirstName());

		List<OneDimensionalTestPojo> pojoList = pojo.getPojoList();
		Assert.assertNotNull("The pojo list cannot be null!", pojoList);
		Assert.assertFalse("The pojo list cannot be empty", pojoList.isEmpty());

		Map<String, OneDimensionalTestPojo> pojoMap = pojo.getPojoMap();
		Assert.assertNotNull("The pojo map cannot be null!", pojoMap);
		Assert.assertFalse("The pojo map cannot be empty!", pojoMap.isEmpty());
	}

	@Test
	public void testEnumsPojo() {

		EnumsPojo pojo = Filler.filling(EnumsPojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		ExternalRatePodamEnum ratePodamExternal = pojo.getRatePodamExternal();
		Assert.assertNotNull("The external enum attribute cannot be null!", ratePodamExternal);

		RatePodamInternal ratePodamInternal = pojo.getRatePodamInternal();

		// Can't test for equality since internal enum is not visible
		Assert.assertNotNull("The internal enum cannot be null!", ratePodamInternal);

	}

	@Test
	public void testPodamStrategyValueAnnotation() {

		StrategyPojo pojo = Filler.filling(StrategyPojo.class);
		Assert.assertNotNull("The post code pojo cannot be null!", pojo);

		String postCode = pojo.getPostCode();
		Assert.assertNotNull("The post code cannot be null!", postCode);
		Assert.assertEquals("The post code does not match the expected value", FillDataTestConstants.POST_CODE, postCode);

		Calendar expectedBirthday = PodamTestUtils.getMyBirthday();

		Calendar myBirthday = pojo.getMyBirthday();

		Assert.assertEquals("The expected and actual calendar objects are not the same", expectedBirthday.getTime(),
				myBirthday.getTime());

		List<Calendar> myBirthdays = pojo.getMyBirthdays();
		Assert.assertNotNull("The birthdays collection cannot be null!", myBirthdays);
		Assert.assertFalse("The birthdays collection cannot be empty!", myBirthdays.isEmpty());

		for (Calendar birthday : myBirthdays) {
			Assert.assertEquals("The expected birthday element does not match the actual", expectedBirthday.getTime(),
					birthday.getTime());
		}

		Calendar[] myBirthdaysArray = pojo.getMyBirthdaysArray();
		Assert.assertNotNull("The birthdays array cannot be null!", myBirthdaysArray);
		Assert.assertFalse("The birthdays array cannot be empty!", myBirthdaysArray.length == 0);

		for (Calendar birthday : myBirthdaysArray) {
			Assert.assertEquals("The expected birthday element does not match the actual", expectedBirthday.getTime(),
					birthday.getTime());
		}

		List<Object> objectList = pojo.getObjectList();
		Assert.assertNotNull("The list of objects cannot be null!", objectList);
		Assert.assertFalse("The list of objects cannot be empty!", objectList.isEmpty());

		Object[] myObjectArray = pojo.getMyObjectArray();
		Assert.assertNotNull("The array of objects cannot be null!", myObjectArray);
		Assert.assertTrue("The array of objects cannot be empty", myObjectArray.length > 0);

		@SuppressWarnings("rawtypes")
		List nonGenericObjectList = pojo.getNonGenericObjectList();
		Assert.assertNotNull("The non generified object list cannot be null!", nonGenericObjectList);
		Assert.assertFalse("The non generified object list cannot be empty!", nonGenericObjectList.isEmpty());

		Map<String, Calendar> myBirthdaysMap = pojo.getMyBirthdaysMap();
		Assert.assertNotNull("The birthday map cannot be null!", myBirthdaysMap);
		Assert.assertFalse("The birthday map cannot be empty!", myBirthdaysMap.isEmpty());

		Set<String> keySet = myBirthdaysMap.keySet();
		for (String key : keySet) {

			Assert.assertEquals("The map element is not my birthday!", expectedBirthday.getTime(),
					myBirthdaysMap.get(key).getTime());

		}

	}

	@Test(expected = PoJoFillException.class)
	public void testStringPojoWithWrongTypeForAnnotationStrategy() {

		Filler.filling(StringWithWrongStrategyTypePojo.class);

	}

	@Test
	public void testPrivateOnlyConstructorPojo() {

		PrivateOnlyConstructorPojo pojo = Filler.filling(PrivateOnlyConstructorPojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);
		Assert.assertNotNull("The string attribute in pojo cannot be null!", pojo.getFirstName());
		Assert.assertTrue("The int field in pojo cannot be zero!", pojo.getIntField() != 0);

	}

	@Test
	public void testNoDefaultPublicConstructorPojo() {

		NoDefaultPublicConstructorPojo pojo = Filler.filling(NoDefaultPublicConstructorPojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);
		Assert.assertNotNull("The string field cannot be null!", pojo.getFirstName());
		Assert.assertTrue("The int field cannot be zero!", pojo.getIntField() != 0);

	}

	@Test
	public void testProtectedNonDefaultConstructorPojo() {
		ProtectedNonDefaultConstructorPojo pojo = Filler.filling(ProtectedNonDefaultConstructorPojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);
		Assert.assertNotNull("The string attribute cannot be null!", pojo.getFirstName());
		Assert.assertTrue("The int field cannot be zero!", pojo.getIntField() != 0);
	}

	@Test
	public void testSomeJavaNativeClasses() {
		String pojo = Filler.filling(String.class);
		Assert.assertNotNull("The generated String object cannot be null!", pojo);

		Integer integerPojo = Filler.filling(Integer.class);
		Assert.assertNotNull("The integer pojo cannot be null!", integerPojo);

		Calendar calendarPojo = Filler.filling(GregorianCalendar.class);
		Assert.assertNotNull("The calendar pojo cannot be null", calendarPojo);

		Date datePojo = Filler.filling(Date.class);
		Assert.assertNotNull("The date pojo cannot be null!", datePojo);

		BigDecimal bigDecimalPojo = Filler.filling(BigDecimal.class);
		Assert.assertNotNull("The Big decimal pojo cannot be null!", bigDecimalPojo);

	}

	// -----------------------------> Private methods

	/**
	 * It checks that the Calendar instance is valid
	 * <p>
	 * If the calendar returns a valid date then it's a valid instance
	 * </p>
	 * 
	 * @param calendarField
	 *            The calendar instance to check
	 */
	private void checkCalendarIsValid(Calendar calendarField) {
		Assert.assertNotNull("The Calendar field cannot be null", calendarField);
		Date calendarDate = calendarField.getTime();
		Assert.assertNotNull("It appears the Calendar field is not valid", calendarDate);
	}

	/**
	 * It validates that the returned list contains the expected values
	 * 
	 * @param list
	 *            The list to verify
	 */
	private void validateReturnedList(List<String> list) {
		Assert.assertNotNull("The List<String> should not be null!", list);
		Assert.assertFalse("The List<String> cannot be empty!", list.isEmpty());
		String element = list.get(0);
		Assert.assertNotNull("The List<String> must have a non-null String element", element);
	}

	/**
	 * It validates that the returned list contains the expected values
	 * 
	 * @param set
	 *            The set to verify
	 */
	private void validateReturnedSet(Set<String> set) {
		Assert.assertNotNull("The Set<String> should not be null!", set);
		Assert.assertFalse("The Set<String> cannot be empty!", set.isEmpty());
		String element = set.iterator().next();
		Assert.assertNotNull("The Set<String> must have a non-null String element", element);
	}

	/**
	 * It validates the {@link HashMap} returned by Podam
	 * 
	 * @param map
	 *            the map to be validated
	 */
	private void validateHashMap(Map<String, OneDimensionalTestPojo> map) {

		Assert.assertTrue("The map attribute must be of type HashMap", map instanceof HashMap);
		Assert.assertNotNull("The map object in the POJO cannot be null", map);
		Set<String> keySet = map.keySet();
		Assert.assertNotNull("The Map must have at least one element", keySet);

		this.validateMapElement(map, keySet);
	}

	/**
	 * It validates the concurrent hash map returned by podam
	 * 
	 * @param map
	 */
	private void validateConcurrentHashMap(ConcurrentMap<String, OneDimensionalTestPojo> map) {

		Assert.assertTrue("The map attribute must be of type ConcurrentHashMap", map instanceof ConcurrentHashMap);
		Assert.assertNotNull("The map object in the POJO cannot be null", map);
		Set<String> keySet = map.keySet();
		Assert.assertNotNull("The Map must have at least one element", keySet);

		this.validateMapElement(map, keySet);
	}

	/**
	 * It validates a map element
	 * 
	 * @param map
	 *            The Map to validate
	 * @param keySet
	 *            The Set of keys in the map
	 */
	private void validateMapElement(Map<String, OneDimensionalTestPojo> map, Set<String> keySet) {
		OneDimensionalTestPojo oneDimensionalTestPojo = map.get(keySet.iterator().next());

		Assert.assertNotNull("The map element must not be null!", oneDimensionalTestPojo);
	}
}
