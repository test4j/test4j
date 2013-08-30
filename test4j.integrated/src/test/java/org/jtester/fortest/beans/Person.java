package org.jtester.fortest.beans;

import org.test4j.fortest.beans.ISpeak;

public class Person {
	private ISpeak speak;

	public ISpeak getSpeak() {
		return speak;
	}

	public void setSpeak(ISpeak speak) {
		this.speak = speak;
	}

	public void sayHelloTo(String name) {
		this.speak.say(String.format("hello,%s!", name));
	}
}
