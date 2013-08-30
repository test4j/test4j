package org.test4j.module.jmockit;

import mockit.Mocked;
import mockit.internal.UnexpectedInvocation;

import org.junit.Test;
import org.test4j.fortest.service.CalledService;
import org.test4j.fortest.service.CallingService;
import org.test4j.junit.Test4J;
import org.test4j.module.inject.annotations.Inject;

public class JMockitExpectationsTest implements Test4J {
    @Mocked
    @Inject(targets = "callingService")
    private CalledService  calledService;

    private CallingService callingService = new CallingService();

    @Test
    public void returnValue() {
        new Expectations() {
            {
                when(calledService.called(the.string().contains("test").wanted())).thenReturn("dddd");
                when(calledService.called(the.string().any().wanted())).callIgnoreTimes();
            }
        };
        callingService.call("i am a test message!");
    }

    @Test
    public void returnValue_ignore() {
        new Expectations() {
            {
                when(calledService.called(the.string().contains("test").wanted())).callIgnoreTimes().thenReturn("dddd");
            }
        };
        callingService.call("i am a test message!");
    }

    @Test(expected = UnexpectedInvocation.class)
    public void returnValue_never() {
        new Expectations() {
            {
                when(calledService.called(anyString)).callNeverOccur();
            }
        };
        callingService.call("i am a test message!");
    }

    @Test(expected = RuntimeException.class)
    public void throwException() {
        new Expectations() {
            {
                when(calledService.called(the.string().contains("test").wanted())).thenThrows(
                        new RuntimeException("test exception"));
            }
        };
        callingService.call("i am a test message!");
    }

    @Test
    public void testThenDoing() {
        new Expectations() {
            {
                calledService.called(any(String.class));
                thenDo(new Delegate() {
                    @SuppressWarnings("unused")
                    public String called(String test) {
                        throw new RuntimeException("test");
                    }
                });
            }
        };
        try {
            callingService.call("i am a test message!");
            want.fail();
        } catch (Exception e) {
            String err = e.getMessage();
            want.string(err).isEqualTo("test");
        }
    }

    @Test
    public void testIs() {
        new Expectations() {
            {
                calledService.called(is("i am a test message!"));
                thenDo(new Delegate() {
                    @SuppressWarnings("unused")
                    public String called(String test) {
                        throw new RuntimeException("test");
                    }
                });
            }
        };
        try {
            callingService.call("i am a test message!");
            want.fail();
        } catch (Exception e) {
            String err = e.getMessage();
            want.string(err).isEqualTo("test");
        }
    }
}
