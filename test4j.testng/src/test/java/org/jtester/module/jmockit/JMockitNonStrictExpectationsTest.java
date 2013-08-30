package org.jtester.module.jmockit;

import mockit.NonStrict;

import ext.jtester.hamcrest.core.IsEqual;
import org.jtester.testng.JTester;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class JMockitNonStrictExpectationsTest extends JTester {
	// @Mocked
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
