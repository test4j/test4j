package org.jtester.datafilling.filler.primitive;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.CharValuePojo;
import org.jtester.datafilling.utils.FillDataTestConstants;
import org.junit.Assert;
import org.junit.Test;

public class CharacterFillerTest {

	@Test
	public void testGetFilling() throws Exception {
		CharValuePojo pojo = Filler.filling(CharValuePojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		char charFieldWithMinValueOnly = pojo.getCharFieldWithMinValueOnly();
		Assert.assertTrue("The char attribute with min value only should have a value greater than "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE,
				charFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_INT_MIN_VALUE);

		char charFieldWithMaxValueOnly = pojo.getCharFieldWithMaxValueOnly();
		Assert.assertTrue("The char attribute with max value only should have a value less or equal than "
				+ FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				charFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		char charFieldWithMinAndMaxValue = pojo.getCharFieldWithMinAndMaxValue();
		Assert.assertTrue("The char attribute with min and max value must have a value between "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE + " and " + FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				charFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_INT_MIN_VALUE
						&& charFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		Character charObjectFieldWithMinValueOnly = pojo.getCharObjectFieldWithMinValueOnly();
		Assert.assertNotNull("The char object attribute with min value only  cannot be null!",
				charObjectFieldWithMinValueOnly);
		Assert.assertTrue("The char object attribute with min value only should have a value greater than "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE,
				charObjectFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_INT_MIN_VALUE);

		Character charObjectFieldWithMaxValueOnly = pojo.getCharObjectFieldWithMaxValueOnly();
		Assert.assertNotNull("The char object attribute with max value only cannot be null!",
				charObjectFieldWithMaxValueOnly);
		Assert.assertTrue("The char object attribute with max value only should have a value less or equal than "
				+ FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				charObjectFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		Character charObjectFieldWithMinAndMaxValue = pojo.getCharObjectFieldWithMinAndMaxValue();
		Assert.assertNotNull("The char object attribute with min and max value cannot be null!",
				charObjectFieldWithMinAndMaxValue);
		Assert.assertTrue("The char object attribute with min and max value must have a value between "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE + " and " + FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				charObjectFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_INT_MIN_VALUE
						&& charObjectFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		char charFieldWithPreciseValue = pojo.getCharFieldWithPreciseValue();
		Assert.assertTrue("The character field with precise value should have a value of "
				+ FillDataTestConstants.CHAR_PRECISE_VALUE,
				charFieldWithPreciseValue == FillDataTestConstants.CHAR_PRECISE_VALUE);

		char charFieldWithBlankInPreciseValue = pojo.getCharFieldWithBlankInPreciseValue();

		Assert.assertTrue(
				"The value for the char field with an empty char in the precise value and no other annotation attributes should be zero",
				charFieldWithBlankInPreciseValue == 0);
	}
}
