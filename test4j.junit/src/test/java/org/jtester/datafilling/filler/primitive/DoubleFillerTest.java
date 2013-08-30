package org.jtester.datafilling.filler.primitive;

import junit.framework.Assert;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.DoubleValuePojo;
import org.jtester.datafilling.annotations.DoubleValueWithErrorPojo;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.utils.FillDataTestConstants;
import org.junit.Test;

public class DoubleFillerTest {

	@Test
	public void testGetFilling() throws Exception {
		DoubleValuePojo pojo = Filler.filling(DoubleValuePojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		double doubleFieldWithMinValueOnly = pojo.getDoubleFieldWithMinValueOnly();
		Assert.assertTrue("The double attribute with min value only must have a value greater than "
				+ FillDataTestConstants.NUMBER_DOUBLE_MIN_VALUE,
				doubleFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_DOUBLE_MIN_VALUE);

		double doubleFieldWithMaxValueOnly = pojo.getDoubleFieldWithMaxValueOnly();
		Assert.assertTrue("The double attribute with max value only must have a value less or equal to "
				+ FillDataTestConstants.NUMBER_DOUBLE_ONE_HUNDRED,
				doubleFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_DOUBLE_ONE_HUNDRED);

		double doubleFieldWithMinAndMaxValue = pojo.getDoubleFieldWithMinAndMaxValue();
		Assert.assertTrue("The double attribute with min and mx value must have a value between "
				+ FillDataTestConstants.NUMBER_DOUBLE_MIN_VALUE + " and " + FillDataTestConstants.NUMBER_DOUBLE_MAX_VALUE,
				doubleFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_DOUBLE_MIN_VALUE
						&& doubleFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_DOUBLE_MAX_VALUE);

		double doubleFieldWithPreciseValue = pojo.getDoubleFieldWithPreciseValue();
		Assert.assertTrue("The double field with precise value should have a value of: "
				+ FillDataTestConstants.DOUBLE_PRECISE_VALUE,
				doubleFieldWithPreciseValue == Double.valueOf(FillDataTestConstants.DOUBLE_PRECISE_VALUE));

		Double doubleObjectFieldWithPreciseValue = pojo.getDoubleObjectFieldWithPreciseValue();
		Assert.assertNotNull("The double object field with precise value cannot be null!",
				doubleObjectFieldWithPreciseValue);
		Assert.assertTrue("The double object field with precise value should have a value of: "
				+ FillDataTestConstants.DOUBLE_PRECISE_VALUE, doubleObjectFieldWithPreciseValue.doubleValue() == Double
				.valueOf(FillDataTestConstants.DOUBLE_PRECISE_VALUE).doubleValue());

	}

	@Test(expected = PoJoFillException.class)
	public void testDoubleValueAnnotationWithError() {
		Filler.filling(DoubleValueWithErrorPojo.class);
	}
}
