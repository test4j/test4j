package org.jtester.module.jmockit;

import mockit.NonStrict;

import org.jtester.module.ICore;
import org.junit.Assert;
import org.junit.Test;

import ext.jtester.hamcrest.core.IsEqual;

//@RunWith(JTesterRunner.class)
public class JMockitNonStrictExpectationsTest implements ICore {
	@NonStrict
	ISay say;

	@Test
	public void testWhen1() {
		new Expectations() {
			{
				say.say(anyString);
				result = "hello, davey.wu";

				say.say(anyString, anyString);
				result = "hello, job.he";
			}
		};

		String result = say.say("davey.wu");
		Assert.assertEquals(result, "hello, davey.wu");
	}

	@Test
	public void testWhen2() {
		new Expectations() {
			{
				say.say(anyString);
				result = "hello, davey.wu";

				say.say(anyString, anyString);
				result = "hello, job.he";
			}
		};
		String result = say.say("davey.wu");
		Assert.assertEquals(result, "hello, davey.wu");
	}

	@Test
	public void testWhen3() {
		new NonStrictExpectations() {
			{
				say.say((String) with(IsEqual.equalTo("davey.wu")));
				result = "hello, davey.wu";

				say.say((String) with(IsEqual.equalTo("job.he")));
				result = "hello, job.he";
			}
		};
		String result = say.say("davey.wu");
		Assert.assertEquals(result, "hello, davey.wu");
	}
}
