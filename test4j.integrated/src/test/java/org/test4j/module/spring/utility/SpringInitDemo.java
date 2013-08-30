package org.test4j.module.spring.utility;

import org.test4j.module.spring.annotations.SpringInitMethod;

public class SpringInitDemo {
	@SpringInitMethod
	private void privateMethod() {
	}

	@SpringInitMethod
	protected void protectedMethod() {
	}

	@SpringInitMethod
	protected void hasParameter(int pi) {

	}

	@SpringInitMethod
	public void publicMethod() {

	}

	public void normalMethod() {

	}
}
