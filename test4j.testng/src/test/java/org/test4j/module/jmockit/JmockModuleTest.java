package org.test4j.module.jmockit;

import mockit.Mocked;

import org.test4j.fortest.beans.ISpeak;
import org.test4j.testng.Test4J;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ext.test4j.hamcrest.MatcherAssert;
import ext.test4j.hamcrest.core.IsEqual;

@Test(groups = { "test4j" })
public class JmockModuleTest extends Test4J {
    @Mocked
    private ISpeak say;

    @BeforeMethod
    public void before() {
        new Expectations() {
            {
                when(say.count()).thenReturn(4);
            }
        };
    }

    @Test
    public void mock_test1() {
        new Expectations() {
            {
                when(say.count()).thenReturn(5);
            }
        };
        int count1 = say.count();
        MatcherAssert.assertThat(count1, IsEqual.equalTo(4));
        int count2 = say.count();
        MatcherAssert.assertThat(count2, IsEqual.equalTo(5));
    }

    @Test
    public void mock_test2() {
        new Expectations() {
            {
                when(say.count()).callIgnoreTimes().thenReturn(3);
            }
        };
        MatcherAssert.assertThat(say.count(), IsEqual.equalTo(4));
        MatcherAssert.assertThat(say.count(), IsEqual.equalTo(3));
        MatcherAssert.assertThat(say.count(), IsEqual.equalTo(3));
    }
}
