package org.jtester.module.spring.utility;

import org.jtester.module.spring.annotations.SpringInitMethod;

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
