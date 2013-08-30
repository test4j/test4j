package org.jtester.junit.statement;

import java.lang.reflect.Method;

import org.jtester.module.core.TestListener;
import org.junit.runners.model.Statement;

/**
 * 在所有的@Before方法之前执行TestListener的setupMethod方法<br>
 * 在所有的@After方法之后执行TestListener的teardownMethod方法
 * 
 * @author darui.wudr
 */
public class TestAroundStatement extends Statement {
	private final TestListener listener;

	private final Statement invoker;

	private final Object test;

	private final Method method;

	public TestAroundStatement(Statement parent, TestListener listener, Object test, Method method) {
		this.listener = listener;
		this.invoker = parent;
		this.test = test;
		this.method = method;
	}

	@Override
	public void evaluate() throws Throwable {
		try {
			this.listener.beforeMethod(this.test, this.method);
			this.invoker.evaluate();
		} finally {
			this.listener.afterMethod(this.test, this.method);
		}
	}
}
