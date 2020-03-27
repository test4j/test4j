package org.test4j.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class Person {
    private ISpeak speak;

    public void sayHelloTo(String name) {
        this.speak.say(String.format("hello,%s!", name));
    }
}
