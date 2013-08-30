package org.test4j.module.jmockit;

import mockit.Mocked;

import org.junit.Test;
import org.test4j.fortest.beans.ISpeak;
import org.test4j.fortest.beans.Person;
import org.test4j.junit.JTester;
import org.test4j.module.inject.annotations.Inject;

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
