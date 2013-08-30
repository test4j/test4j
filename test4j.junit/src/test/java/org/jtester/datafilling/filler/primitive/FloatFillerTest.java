package org.jtester.datafilling.filler.primitive;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.FloatValuePojo;
import org.jtester.datafilling.annotations.FloatValueWithErrorPojo;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.utils.FillDataTestConstants;
import org.junit.Assert;
import org.junit.Test;

public class FloatFillerTest {

	@Test
	public void testGetFilling() throws Exception {
		FloatValuePojo pojo = Filler.filling(FloatValuePojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		float floatFieldWithMinValueOnly = pojo.getFloatFieldWithMinValueOnly();
		Assert.assertTrue("The float field with min value only must have value greater than "
				+ FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE,
				floatFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE);

		float floatFieldWithMaxValueOnly = pojo.getFloatFieldWithMaxValueOnly();
		Assert.assertTrue("The float field with max value only can only have a value less or equal than "
				+ FillDataTestConstants.NUMBER_FLOAT_ONE_HUNDRED,
				floatFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_FLOAT_ONE_HUNDRED);

		float floatFieldWithMinAndMaxValue = pojo.getFloatFieldWithMinAndMaxValue();
		Assert.assertTrue("The float field with min and max value must have a value between "
				+ FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE + " and " + FillDataTestConstants.NUMBER_FLOAT_MAX_VALUE,
				floatFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE
						&& floatFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_FLOAT_MAX_VALUE);

		Float floatObjectFieldWithMinValueOnly = pojo.getFloatObjectFieldWithMinValueOnly();
		Assert.assertNotNull("The float object attribute with min value only cannot be null!",
				floatObjectFieldWithMinValueOnly);
		Assert.assertTrue("The float object attribute with min value only must have a value greater or equal than "
				+ FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE,
				floatObjectFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE);

		Float floatObjectFieldWithMaxValueOnly = pojo.getFloatObjectFieldWithMaxValueOnly();
		Assert.assertNotNull("The float object attribute with max value only cannot be null!",
				floatObjectFieldWithMaxValueOnly);
		Assert.assertTrue("The float object attribute with max value only must have a value less than or equal to "
				+ FillDataTestConstants.NUMBER_FLOAT_ONE_HUNDRED,
				floatObjectFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_FLOAT_ONE_HUNDRED);

		Float floatObjectFieldWithMinAndMaxValue = pojo.getFloatObjectFieldWithMinAndMaxValue();
		Assert.assertNotNull("The float object attribute with min and max value cannot be null!",
				floatObjectFieldWithMinAndMaxValue);
		Assert.assertTrue("The float object attribute with min and max value only must have a value between "
				+ FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE + " and " + FillDataTestConstants.NUMBER_FLOAT_MAX_VALUE,
				floatObjectFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE
						&& floatObjectFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_FLOAT_MAX_VALUE);

		float floatFieldWithPreciseValue = pojo.getFloatFieldWithPreciseValue();
		Assert.assertTrue("The float field with precise value should have a value of "
				+ FillDataTestConstants.FLOAT_PRECISE_VALUE,
				floatFieldWithPreciseValue == Float.valueOf(FillDataTestConstants.FLOAT_PRECISE_VALUE));

		Float floatObjectFieldWithPreciseValue = pojo.getFloatObjectFieldWithPreciseValue();
		Assert.assertNotNull("The float object field with precise value cannot be null!",
				floatObjectFieldWithPreciseValue);
		Assert.assertTrue("The float object field with precise value should have a value of "
				+ FillDataTestConstants.FLOAT_PRECISE_VALUE, floatObjectFieldWithPreciseValue.floatValue() == Float
				.valueOf(FillDataTestConstants.FLOAT_PRECISE_VALUE).floatValue());

	}

	@Test(expected = PoJoFillException.class)
	public void testFloatValueAnnotationWithNumberFormatError() {
		Filler.filling(FloatValueWithErrorPojo.class);
	}
}
