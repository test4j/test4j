package org.jtester.module.jmockit;

import mockit.Mocked;

import org.jtester.fortest.beans.ISpeak;
import org.jtester.fortest.beans.Person;
import org.jtester.junit.JTester;
import org.jtester.module.inject.annotations.Inject;
import org.junit.Test;

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
