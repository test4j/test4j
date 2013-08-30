package org.jtester.module.jmockit.demo;

import java.util.Observable;
import java.util.concurrent.Callable;

import mockit.Capturing;
import mockit.Mocked;
import mockit.Verifications;

import org.jtester.junit.JTester;
import org.junit.Test;

public class InternalInstancesDemoTest implements JTester {
    @Mocked
    @Capturing(maxInstances = 10)
    Service service;

    @Test
    public void captureAllInternallyCreatedInstances(@Mocked @Capturing(maxInstances = 1) final Callable<?> callable)
            throws Exception {
        Service initialMockService = service;

        new NonStrictExpectations() {
            @Mocked
            @Capturing(maxInstances = 1)
            Observable observable;
            {
                service.doSomething();
                returns(3, 4);
            }
        };

        InternalInstancesDemo unit = new InternalInstancesDemo();
        int result = unit.businessOperation(true);

        want.object(unit.observable).notNull();
        want.object(initialMockService).not(the.object().same(service));
        want.number(result).isEqualTo(7);

        new Verifications() {
            {
                callable.call();
            }
        };
    }
}
