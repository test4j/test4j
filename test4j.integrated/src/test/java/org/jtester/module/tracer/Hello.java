package org.jtester.module.tracer;

public class Hello {
	public String greeting() {
		return "greeting";
	}

	public String greetingException() {
		throw new RuntimeException("runtime exception");
	}
}
