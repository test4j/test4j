package org.jtester.module.jmockit;

import java.lang.reflect.Method;

import mockit.internal.startup.Startup;

import org.jtester.module.core.Module;
import org.jtester.module.core.TestListener;
import org.jtester.module.core.utility.MessageHelper;

public class JMockitModule implements Module {

	public void init() {
		MessageHelper.info("init jmockit in jmockit module.");

		Startup.initializeIfNeeded();
	}

	public void afterInit() {
	}

	public TestListener getTestListener() {
		return new JMockitTestListener();
	}

	protected class JMockitTestListener extends TestListener {
		/**
		 * {@inheritDoc}<br>
		 * jmockit-0.999.9 bug fixed:<br>
		 * 测试类在 mockit.integration.testng.internal.TestNGRunnerDecorator 方法<br>
		 * run(IHookCallBack callBack, ITestResult testResult) -><br>
		 * executeTestMethod(IHookCallBack callBack, ITestResult testResult) -><br>
		 * cleanUpAfterTestMethodExecution() -><br>
		 * ExecutingTest..void finishExecution()中将全局的 nonStrictMocks
		 * 列表清空，导致后续的Expectations()出错
		 */
		public void beforeMethod(Object testObject, Method testMethod) {
			// JMockitHookable.reRegistedMockField(testObject);
		}

		@Override
		public void afterClass(Object testObject) {
			// Mockit.tearDownMocks();
		}

		@Override
		protected String getName() {
			return "JMockitTestListener";
		}
	}
}
