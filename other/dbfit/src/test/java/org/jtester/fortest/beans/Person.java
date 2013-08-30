package org.jtester.fortest.beans;

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
