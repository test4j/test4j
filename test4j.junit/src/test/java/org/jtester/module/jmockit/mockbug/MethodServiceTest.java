package org.jtester.module.jmockit.mockbug;

import mockit.Mock;

import org.jtester.junit.JTester;
import org.junit.Before;
import org.junit.Test;

public class MethodServiceTest implements JTester {

    @Before
    public void setup() {
        new MockUp<TestedMethodService>() {
            @Mock
            public void $init() {

            }

            @Mock
            public String sayHello() {
                return "hello,mock!";
            }
        };
    }

    @Test
    public void testBeforeMethodMock() {
        TestedMethodService service = new TestedMethodService();
        String result = service.sayHello();
        System.out.println(result);
        want.string(result).isEqualTo("hello,mock!");

        String name = reflector.getField(service, "name");
        want.string(name).isNull();
    }
}
