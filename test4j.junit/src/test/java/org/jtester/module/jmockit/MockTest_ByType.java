package org.jtester.module.jmockit;

import mockit.Mocked;

import org.jtester.fortest.beans.ISpeak;
import org.jtester.fortest.beans.Person;
import org.jtester.junit.JTester;
import org.jtester.module.inject.annotations.Inject;
import org.junit.Test;

public class MockTest_ByType implements JTester {
    // @TestedObject
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
    public void sayHello_2() {
        new Expectations() {
            {
                speak.say(the.string().contains("darui.wu").wanted());
            }
        };
        person.sayHelloTo("darui.wu");
    }

    @Test
    public void sayHello_3() {
        new Expectations() {
            {
                speak.say((String) with(the.string().contains("darui.wu")));
            }
        };
        person.sayHelloTo("darui.wu");
    }
}
