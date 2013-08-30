package org.jtester.junit.statement;

import java.lang.reflect.Method;

import org.jtester.module.core.TestListener;
import org.junit.runners.model.Statement;

/**
 * 在所有的@Before方法之后，测试方法之前执行TestListener的beforeMethodRunning方法<br>
 * 在所有的@After方法之前，测试方法之后执行TestListener的afterMethodRunned方法
 * 
 * @author darui.wudr
 */
public class MethodAroundStatement extends Statement {
	private final Statement invoker;

	private final TestListener testListener;

	private final Object test;

	private final Method method;

	public MethodAroundStatement(Statement invoker, TestListener testListener, Object test, Method method) {
		this.invoker = invoker;
		this.testListener = testListener;
		this.test = test;
		this.method = method;
	}

	@Override
	public void evaluate() throws Throwable {
		Throwable methodE = null;
		try {
			testListener.beforeRunning(this.test, this.method);
			invoker.evaluate();
		} catch (Throwable e) {
			methodE = e;
			throw e;
		} finally {
			testListener.afterRunned(this.test, this.method, methodE);
		}
	}
}
