package org.test4j.module.jmockit;

import mockit.Mocked;

import org.test4j.fortest.beans.ISpeak;
import org.test4j.fortest.beans.Person;
import org.test4j.module.inject.annotations.Inject;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j" })
public class JmockModuleTest_Two extends Test4J {

    private final Person person = new Person();

    @Mocked
    @Inject(targets = "person")
    private ISpeak       speak;

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
                // result = new RuntimeException("test");
                thenThrow(new RuntimeException("test"));
            }
        };
        try {
            person.sayHelloTo("darui.wu");
        } catch (Throwable e) {
            String message = e.getMessage();
            want.string(message).contains("test");
        }
    }
}
