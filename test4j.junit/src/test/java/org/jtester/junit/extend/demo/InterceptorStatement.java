package org.jtester.junit.extend.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.Statement;

public class InterceptorStatement extends Statement {
	private final Statement invoker;
	private List<Interceptor> interceptors = new ArrayList<Interceptor>();

	public InterceptorStatement(Statement invoker) {
		this.invoker = invoker;
	}

	@Override
	public void evaluate() throws Throwable {
		for (Interceptor interceptor : interceptors) {
			interceptor.interceptBefore();
		}
		invoker.evaluate();
		for (Interceptor interceptor : interceptors) {
			interceptor.interceptAfter();
		}
	}

	public void addInterceptor(Interceptor interceptor) {
		interceptors.add(interceptor);
	}
}
