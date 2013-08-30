package org.test4j.module.jmockit;

import mockit.Mocked;

import org.junit.Test;
import org.test4j.fortest.beans.ISpeak;
import org.test4j.fortest.beans.Person;
import org.test4j.junit.JTester;
import org.test4j.module.inject.annotations.Inject;

public class JmockModuleTest_Two implements JTester {

    private Person person = new Person();

    @Mocked
    @Inject(targets = "person")
    private ISpeak speak;

    @Test
    public void sayHello() {
        new Expectations() {
            {
                speak.say(the.string().contains("darui.wu").wanted());
            }
        };
        person.sayHelloTo("darui.wu");
    }

    @Test
    public void sayHello_ThrowRuntimeException_WithoutWrapBySpring() {
        new Expectations() {
            {
                speak.say(the.string().contains("darui.wu").wanted());
                // result = new RuntimeException("testedObject");
                thenThrow(new RuntimeException("testedObject"));
            }
        };
        try {
            person.sayHelloTo("darui.wu");
        } catch (Throwable e) {
            String message = e.getMessage();
            want.string(message).contains("testedObject");
        }
    }
}
