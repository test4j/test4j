package org.jtester.datafilling.filler.primitive;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.ByteValuePojo;
import org.jtester.datafilling.utils.FillDataTestConstants;
import org.junit.Assert;
import org.junit.Test;

public class ByteFillerTest {

	@Test
	public void testGetFilling() throws Exception {
		ByteValuePojo pojo = Filler.filling(ByteValuePojo.class);
		Assert.assertNotNull("The Pojo cannot be null!", pojo);
		byte byteFieldWithMinValueOnly = pojo.getByteFieldWithMinValueOnly();
		Assert.assertTrue("The byte field with min value only should have a minimum value of zero!",
				byteFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_INT_MIN_VALUE);
		byte byteFieldWithMaxValueOnly = pojo.getByteFieldWithMaxValueOnly();
		Assert.assertTrue("The byte field value cannot be greater than: " + FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				byteFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);
		byte byteFieldWithMinAndMaxValue = pojo.getByteFieldWithMinAndMaxValue();
		Assert.assertTrue("The byte field value must be between: " + FillDataTestConstants.NUMBER_INT_MIN_VALUE + " and "
				+ FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				byteFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_INT_MIN_VALUE
						&& byteFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);
		Byte byteObjectFieldWithMinValueOnly = pojo.getByteObjectFieldWithMinValueOnly();
		Assert.assertNotNull("The byte object with min value only cannot be null!", byteObjectFieldWithMinValueOnly);
		Assert.assertTrue("The byte object value must be greate or equal than: "
				+ FillDataTestConstants.NUMBER_INT_MIN_VALUE,
				byteObjectFieldWithMinValueOnly >= FillDataTestConstants.NUMBER_INT_MIN_VALUE);

		Byte byteObjectFieldWithMaxValueOnly = pojo.getByteObjectFieldWithMaxValueOnly();
		Assert.assertNotNull("The byte object field cannot be null", byteObjectFieldWithMaxValueOnly);
		Assert.assertTrue("The byte object field must have a value less or equal to  "
				+ FillDataTestConstants.NUMBER_INT_ONE_HUNDRED,
				byteObjectFieldWithMaxValueOnly <= FillDataTestConstants.NUMBER_INT_ONE_HUNDRED);

		Byte byteObjectFieldWithMinAndMaxValue = pojo.getByteObjectFieldWithMinAndMaxValue();
		Assert.assertNotNull("The byte object must not be null!", byteObjectFieldWithMinAndMaxValue);
		Assert.assertTrue("The byte object must have a value between: " + FillDataTestConstants.NUMBER_INT_MIN_VALUE
				+ " and " + FillDataTestConstants.NUMBER_INT_MAX_VALUE,
				byteObjectFieldWithMinAndMaxValue >= FillDataTestConstants.NUMBER_INT_MIN_VALUE
						&& byteObjectFieldWithMinAndMaxValue <= FillDataTestConstants.NUMBER_INT_MAX_VALUE);
		byte byteFieldWithPreciseValue = pojo.getByteFieldWithPreciseValue();
		Assert.assertTrue("The byte with precise value should have value: " + FillDataTestConstants.BYTE_PRECISE_VALUE,
				byteFieldWithPreciseValue == Byte.valueOf(FillDataTestConstants.BYTE_PRECISE_VALUE));
	}
}
