package org.jtester.module.tracer;

public class NoNullConstructor {
	public NoNullConstructor(String arg) {

	}

	public String greeting() {
		return "greeting";
	}

	public String greetingException() {
		throw new RuntimeException("runtime exception");
	}
}
