package org.jtester.module.jmockit.demo;

public interface Service {
	int doSomething();

	public static final class ServiceImpl implements Service {
		public int doSomething() {
			return 1;
		}
	}
}
