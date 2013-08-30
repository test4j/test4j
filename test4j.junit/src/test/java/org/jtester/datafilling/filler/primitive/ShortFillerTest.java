package org.jtester.datafilling.filler.primitive;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.ShortValuePojo;
import org.jtester.datafilling.annotations.ShortValueWithErrorPojo;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.utils.FillDataTestConstants;
import org.junit.Assert;
import org.junit.Test;

public class ShortFillerTest {

	@Test
	public void testGetFilling() throws Exception {
		ShortValuePojo pojo = Filler.filling(ShortValuePojo.class);
		Assert.assertNotNull("The Pojo cannot be null!", pojo);

		short shortFieldWithMinValueOnly = pojo.getShortFieldWithMinValueOnly();
		Assert.assertTrue("The short attribute with min value only should have a value greater than "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE,
				shortFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_INT_MIN_VALUE);

		short shortFieldWithMaxValueOnly = pojo.getShortFieldWithMaxValueOnly();
		Assert.assertTrue("The short attribute with max value only should have a value less than: "
				+ FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				shortFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		short shortFieldWithMinAndMaxValue = pojo.getShortFieldWithMinAndMaxValue();
		Assert.assertTrue("The short field with min and max values should have a value beetween "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE + " and " + FillDataTestConstants.NUMBER_INT_MAX_VALUE,
				shortFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_INT_MIN_VALUE
						&& shortFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		Short shortObjectFieldWithMinValueOnly = pojo.getShortObjectFieldWithMinValueOnly();
		Assert.assertNotNull("The short object field with min value only should not be null!",
				shortObjectFieldWithMinValueOnly);
		Assert.assertTrue("The short object attribute with min value only should have a value greater than "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE,
				shortObjectFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_INT_MIN_VALUE);

		Short shortObjectFieldWithMaxValueOnly = pojo.getShortObjectFieldWithMaxValueOnly();
		Assert.assertNotNull("The short object field with max value only should not be null!",
				shortObjectFieldWithMaxValueOnly);
		Assert.assertTrue("The short object attribute with max value only should have a value less than: "
				+ FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				shortObjectFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		Short shortObjectFieldWithMinAndMaxValue = pojo.getShortObjectFieldWithMinAndMaxValue();
		Assert.assertNotNull("The short object field with max value only should not be null!",
				shortObjectFieldWithMinAndMaxValue);
		Assert.assertTrue("The short object field with min and max values should have a value beetween "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE + " and " + FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				shortObjectFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_INT_MIN_VALUE
						&& shortObjectFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		short shortFieldWithPreciseValue = pojo.getShortFieldWithPreciseValue();
		Assert.assertTrue("The short attribute with precise value should have a value of "
				+ FillDataTestConstants.SHORT_PRECISE_VALUE + " but instead it had a value of "
				+ shortFieldWithPreciseValue,
				shortFieldWithPreciseValue == Short.valueOf(FillDataTestConstants.SHORT_PRECISE_VALUE));
	}

	@Test(expected = PoJoFillException.class)
	public void testShortValueAnnotationWithNumberFormatException() {
		Filler.filling(ShortValueWithErrorPojo.class);
	}
}
