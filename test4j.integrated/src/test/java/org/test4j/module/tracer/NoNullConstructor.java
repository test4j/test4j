package org.test4j.module.tracer;

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
