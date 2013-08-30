package org.jtester.datafilling.filler.primitive;

import junit.framework.Assert;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.IntegerValuePojo;
import org.jtester.datafilling.annotations.IntegerValueWithErrorPojo;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.utils.FillDataTestConstants;
import org.jtester.module.ICore;
import org.junit.Test;

public class IntegerFillerTest implements ICore {

	@Test
	public void testFillWith() throws Exception {
		Integer intValue = Filler.filling(int.class);
		want.number(intValue).notEqualTo(0);
	}

	@Test
	public void testIntegerFilling() {
		IntegerValuePojo pojo = Filler.filling(IntegerValuePojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);
		int intFieldWithMinValueOnly = pojo.getIntFieldWithMinValueOnly();
		Assert.assertTrue("The int field with only minValue should be >= 0", intFieldWithMinValueOnly >= 0);
		int intFieldWithMaxValueOnly = pojo.getIntFieldWithMaxValueOnly();
		Assert.assertTrue("The int field with maximum value only should have a maximum value of 100",
				intFieldWithMaxValueOnly <= 100);
		int intObjectFieldWithMinAndMaxValue = pojo.getIntFieldWithMinAndMaxValue();
		Assert.assertTrue("The int field with both min and max value should have a value comprised between",
				intObjectFieldWithMinAndMaxValue >= 0 && intObjectFieldWithMinAndMaxValue <= 1000);
		Integer integerObjectFieldWithMinValueOnly = pojo.getIntegerObjectFieldWithMinValueOnly();
		Assert.assertNotNull("The integer field with minimum value only should not be null!",
				integerObjectFieldWithMinValueOnly);
		Assert.assertTrue(
				"The integer field with minimum value only should have a minimum value greater or equal to zero!",
				integerObjectFieldWithMinValueOnly.intValue() >= 0);
		Integer integerObjectFieldWithMaxValueOnly = pojo.getIntegerObjectFieldWithMaxValueOnly();
		Assert.assertNotNull("The integer field with maximum value only should not be null!",
				integerObjectFieldWithMaxValueOnly);
		Assert.assertTrue("The integer field with maximum value only should have a maximum value of 100",
				integerObjectFieldWithMaxValueOnly.intValue() <= 100);
		Integer integerObjectFieldWithMinAndMaxValue = pojo.getIntegerObjectFieldWithMinAndMaxValue();
		Assert.assertNotNull("The integer field with minimum and maximum value should not be null!",
				integerObjectFieldWithMinAndMaxValue);
		Assert.assertTrue(
				"The integer field with minimum and maximum value should have value comprised between 0 and 1000",
				integerObjectFieldWithMinAndMaxValue.intValue() >= 0
						&& integerObjectFieldWithMinAndMaxValue.intValue() <= 1000);

		int intFieldWithPreciseValue = pojo.getIntFieldWithPreciseValue();
		Assert.assertTrue("The integer field with precise value must have a value of: "
				+ FillDataTestConstants.INTEGER_PRECISE_VALUE,
				intFieldWithPreciseValue == Integer.valueOf(FillDataTestConstants.INTEGER_PRECISE_VALUE));

		Integer integerObjectFieldWithPreciseValue = pojo.getIntegerObjectFieldWithPreciseValue();
		Assert.assertNotNull("The integer object field with precise value cannot be null!",
				integerObjectFieldWithPreciseValue);
		Assert.assertTrue("The integer object field with precise value should have a value of "
				+ FillDataTestConstants.INTEGER_PRECISE_VALUE, integerObjectFieldWithPreciseValue.intValue() == Integer
				.valueOf(FillDataTestConstants.INTEGER_PRECISE_VALUE));
	}

	@Test(expected = PoJoFillException.class)
	public void testInteger_AnnotationWithNumberFormatError() {
		Filler.filling(IntegerValueWithErrorPojo.class);
	}
}
