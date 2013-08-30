package org.test4j.module.jmockit;

import mockit.NonStrict;

import org.test4j.testng.Test4J;
import org.testng.Assert;
import org.testng.annotations.Test;

import ext.test4j.hamcrest.core.IsEqual;

@Test(groups = "test4j")
public class JMockitNonStrictExpectationsTest extends Test4J {
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
