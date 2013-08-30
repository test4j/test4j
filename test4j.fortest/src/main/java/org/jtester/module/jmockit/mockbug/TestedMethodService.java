package org.jtester.module.jmockit.mockbug;

public class TestedMethodService {

	private String name = null;

	public TestedMethodService() {
		this.name = "init construction";
	}

	public String sayHello() {
		return "hello, " + name;
	}
}
