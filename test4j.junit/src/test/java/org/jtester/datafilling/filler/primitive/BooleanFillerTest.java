package org.jtester.datafilling.filler.primitive;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.annotations.BooleanValuePojo;
import org.junit.Assert;
import org.junit.Test;

public class BooleanFillerTest {

	@Test
	public void testGetFilling() throws Exception {
		BooleanValuePojo pojo = Filler.filling(BooleanValuePojo.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		boolean boolDefaultToTrue = pojo.isBoolDefaultToTrue();
		Assert.assertTrue("The boolean attribute forced to true should be true!", boolDefaultToTrue);

		boolean boolDefaultToFalse = pojo.isBoolDefaultToFalse();
		Assert.assertFalse("The boolean attribute forced to false should be false!", boolDefaultToFalse);

		Boolean boolObjectDefaultToFalse = pojo.getBoolObjectDefaultToFalse();
		Assert.assertNotNull("The boolean object forced to false should not be null!", boolObjectDefaultToFalse);
		Assert.assertFalse("The boolean object forced to false should have a value of false!", boolObjectDefaultToFalse);

		Boolean boolObjectDefaultToTrue = pojo.getBoolObjectDefaultToTrue();
		Assert.assertNotNull("The boolean object forced to true should not be null!", boolObjectDefaultToTrue);
		Assert.assertTrue("The boolean object forced to true should have a value of true!", boolObjectDefaultToTrue);
	}
}
