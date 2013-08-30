package org.jtester.testng;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jtester.module.ICore;
import org.jtester.module.core.TestContext;
import org.jtester.module.core.TestListener;
import org.jtester.module.core.utility.ListenerExecutor;
import org.jtester.module.core.utility.ModulesManager;
import org.jtester.module.jmockit.JMockitModule;
import org.jtester.tools.commons.ExceptionWrapper;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * Base testNG class that will enable specify features.
 * 
 * @author darui.wudr
 */
public abstract class JTesterHookable implements IHookable, ICore {
	protected final static String TEST_CLAZZ_INFO = "%s executing test class[%s] in thread[%d].";

	protected final static String TEST_METHOD_INFO = "%s executing test method[%s . %s ()] in thread[%d].";

	/**
	 * jTester @BeforeClass 方法抛出的异常
	 */
	protected Throwable error_setup_class = null;

	/**
	 * jTester @BeforeMethod 方法抛出的异常
	 */
	protected Throwable error_setup_method = null;

	/**
	 * Implementation of the hookable interface to be able to call
	 * {@link TestListener#beforeTestMethod} and
	 * {@link TestListener#afterTestMethod}.
	 * 
	 * @param callBack
	 *            the TestNG test callback, not null
	 * @param testResult
	 *            the TestNG test result, not null
	 */
	public void run(IHookCallBack callBack, ITestResult testResult) {
		this.doesMethodHasSetupError();

		Method testedMethod = testResult.getMethod().getConstructorOrMethod().getMethod();
		Throwable beforeRunningError = ListenerExecutor.executeBeforeRunningEvents(this, testedMethod);

		Throwable testedMethodError = null;
		try {
			if (beforeRunningError == null) {
				callBack.runTestMethod(testResult);
				testedMethodError = this.getTestedMethodError(testResult);
			}
		} catch (Throwable e) {
			testedMethodError = this.getTestedMethodError(testResult);
			if (testedMethodError == null) {
				throw TestContext.getMultipleException(e);
			}
		}

		Throwable afterRunnedError = ListenerExecutor.executeAfterRunnedEvents(this, testedMethod,
				beforeRunningError != null ? beforeRunningError : testedMethodError);
		this.doesThrowExceptionTeardown(beforeRunningError, testedMethodError, afterRunnedError);
	}

	/**
	 * 测试方法是否已经在@BeforeClass和@BeforeMethod中已经有hold的异常，如果有，则抛出异常
	 */
	private void doesMethodHasSetupError() {
		ExceptionWrapper.throwRuntimeException("jtester tested class setup error.", error_setup_class);

		ExceptionWrapper.throwRuntimeException("jtester tested method setup error", error_setup_method);
	}

	/**
	 * 测试方法结束后是否抛出异常<br>
	 * 只抛出beforeRunning和afterRunned的异常，testedMethod的异常testng自己会处理<br>
	 * We don't throw the testMethodException, it is already registered by
	 * TestNG and will be reported to the user
	 * 
	 * @param beforeRunningError
	 * @param testedMethodError
	 * @param afterRunnedError
	 */
	private void doesThrowExceptionTeardown(Throwable beforeRunningError, Throwable testedMethodError,
			Throwable afterRunnedError) {
		ExceptionWrapper.throwRuntimeException(beforeRunningError);

		Throwable teardowError = testedMethodError != null ? null : afterRunnedError;
		ExceptionWrapper.throwRuntimeException(teardowError);
	}

	/**
	 * 获取测试执行过程中的异常<br>
	 * Since TestNG calls the method using reflection<br>
	 * the exception is wrapped in an InvocationTargetException
	 * 
	 * @param testResult
	 *            测试结果
	 * @return 异常
	 */
	private Throwable getTestedMethodError(ITestResult testResult) {
		Throwable testedMethodError = testResult.getThrowable();
		if (testedMethodError != null && testedMethodError instanceof InvocationTargetException) {
			testedMethodError = ((InvocationTargetException) testedMethodError).getTargetException();
		}
		return testedMethodError;
	}

	private Boolean isJMockitEnabled = null;

	protected void dealJMockitTestDecorator(ITestContext context) {
		if (isJMockitEnabled != null) {
			return;
		}
		this.isJMockitEnabled = ModulesManager.isModuleEnabled(JMockitModule.class);
		if (this.isJMockitEnabled == false) {
			return;
		}
		new JMockitInvokaeMethodListener(context);
		this.isJMockitEnabled = true;
	}
}
