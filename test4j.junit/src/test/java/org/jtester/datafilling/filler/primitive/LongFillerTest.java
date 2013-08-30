package org.jtester.datafilling.filler.primitive;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.LongValuePojo;
import org.jtester.datafilling.annotations.LongValueWithErrorPojo;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.utils.FillDataTestConstants;
import org.junit.Assert;
import org.junit.Test;

public class LongFillerTest {

	@Test
	public void testGetFilling() throws Exception {
		LongValuePojo pojo = Filler.filling(LongValuePojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);
		long longFieldWithMinValueOnly = pojo.getLongFieldWithMinValueOnly();
		Assert.assertTrue("The long field with min value only should have a value >= 0", longFieldWithMinValueOnly >= 0);
		long longFieldWithMaxValueOnly = pojo.getLongFieldWithMaxValueOnly();
		Assert.assertTrue("The long field with maximumm value only should have a maximum value of 100",
				longFieldWithMaxValueOnly <= 100);
		long longFieldWithMinAndMaxValue = pojo.getLongFieldWithMinAndMaxValue();
		Assert.assertTrue(
				"The long field with both min and max value should have a value comprised between 0 and 1000!",
				longFieldWithMinAndMaxValue >= 0 && longFieldWithMinAndMaxValue <= 1000);

		Long longObjectFieldWithMinValueOnly = pojo.getLongObjectFieldWithMinValueOnly();
		Assert.assertNotNull("The Long Object field with min value only cannot be null!",
				longObjectFieldWithMinValueOnly);
		Assert.assertTrue("The Long Object field with min value only should have a value >= 0",
				longObjectFieldWithMinValueOnly >= 0);

		Long longObjectFieldWithMaxValueOnly = pojo.getLongObjectFieldWithMaxValueOnly();
		Assert.assertNotNull("The Long Object field with max value only cannot be null!",
				longObjectFieldWithMaxValueOnly);
		Assert.assertTrue("The Long Object field with max value only should have a value <= 100",
				longObjectFieldWithMaxValueOnly <= 100);

		Long longObjectFieldWithMinAndMaxValue = pojo.getLongObjectFieldWithMinAndMaxValue();
		Assert.assertNotNull("The Long Object field with min and max value cannot be null!",
				longObjectFieldWithMinAndMaxValue);
		Assert.assertTrue(
				"The Long object field with min and max value should have a value comprised between 0 and 1000",
				longObjectFieldWithMinAndMaxValue >= 0L && longObjectFieldWithMinAndMaxValue <= 1000L);

		long longFieldWithPreciseValue = pojo.getLongFieldWithPreciseValue();
		Assert.assertTrue("The long field with precise value must have a value of "
				+ FillDataTestConstants.LONG_PRECISE_VALUE,
				longFieldWithPreciseValue == Long.valueOf(FillDataTestConstants.LONG_PRECISE_VALUE));

		Long longObjectFieldWithPreciseValue = pojo.getLongObjectFieldWithPreciseValue();
		Assert.assertNotNull("The long object with precise value should not be null!", longObjectFieldWithPreciseValue);
		Assert.assertTrue("The long object field with precise value must have a value of "
				+ FillDataTestConstants.LONG_PRECISE_VALUE,
				longObjectFieldWithPreciseValue.longValue() == Long.valueOf(FillDataTestConstants.LONG_PRECISE_VALUE)
						.longValue());
	}

	@Test(expected = PoJoFillException.class)
	public void testLongValueAnnotationWithNumberFormatException() {
		Filler.filling(LongValueWithErrorPojo.class);
	}
}
