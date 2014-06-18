package org.test4j.module.jmockit;

import mockit.Mocked;

import org.junit.Assert;
import org.junit.Test;
import org.test4j.module.ICore;

import ext.test4j.hamcrest.core.IsEqual;

//@RunWith(Test4JRunner.class)
public class JMockitNonStrictExpectationsTest implements ICore {
    @Mocked
    ISay say;

    @Test
    public void testWhen1() {
        new NonStrictExpectations() {
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
        new NonStrictExpectations() {
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
