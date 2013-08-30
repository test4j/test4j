package org.jtester.module.core.utility;

import java.lang.reflect.Method;

import org.jtester.module.core.CoreModule;
import org.jtester.module.core.TestListener;

/**
 * jTester Listener执行器
 * 
 * @author darui.wudr
 * 
 */
public class ListenerExecutor {
	private final static String TEST_CLAZZ_INFO = "%s executing test class[%s] in thread[%d].";

	private final static String TEST_METHOD_INFO = "%s executing test method[%s . %s ()] in thread[%d].";

	/**
	 * 执行setup class的事件
	 * 
	 * @param testedObject
	 *            测试对象
	 * @return 事件异常
	 */
	@SuppressWarnings("rawtypes")
	public static Throwable executeBeforeClassEvents(Class testClazz) {
		String hits = String.format(TEST_CLAZZ_INFO, "Begin", testClazz.getName(), Thread.currentThread().getId());
		MessageHelper.info("\n\n\n" + hits);

		try {
			getTestListener().beforeClass(testClazz);
			return null;
		} catch (Throwable e) {
			e.printStackTrace();
			return e;
		}
	}

	/**
	 * 执行setup method事件
	 * 
	 * @param testedObject
	 *            测试对象
	 * @param testedMethod
	 *            测试方法
	 * @return 事件异常
	 */
	public static Throwable executeBeforeMethodEvents(Object testedObject, Method testedMethod) {
		String hits = String.format(TEST_METHOD_INFO, "Begin", testedObject.getClass().getName(),
				testedMethod.getName(), Thread.currentThread().getId());
		MessageHelper.info("\n" + hits);

		try {
			getTestListener().beforeMethod(testedObject, testedMethod);
			return null;
		} catch (Throwable e) {
			e.printStackTrace();
			return e;
		}
	}

	/**
	 * 执行测试方法开始的事件
	 * 
	 * @param testedMethod
	 *            测试方法
	 * @return 返回事件异常
	 */
	public static Throwable executeBeforeRunningEvents(Object testedObject, Method testedMethod) {
		try {
			getTestListener().beforeRunning(testedObject, testedMethod);
			return null;
		} catch (Throwable e) {
			return e;
		}
	}

	/**
	 * 执行测试方法完毕后的事件
	 * 
	 * @param testedMethod
	 *            测试方法
	 * @param error
	 *            测试方法异常
	 * @return 返回事件异常
	 */
	public static Throwable executeAfterRunnedEvents(Object testedObject, Method testedMethod, Throwable error) {
		try {
			getTestListener().afterRunned(testedObject, testedMethod, error);
			return null;
		} catch (Throwable e) {
			return e;
		}
	}

	/**
	 * 执行teardown method事件
	 * 
	 * @param testedObject
	 *            测试对象
	 * @param testedMethod
	 *            测试方法
	 * @return 事件异常
	 */
	public static Throwable executeAfterMethodEvents(Object testedObject, Method testedMethod) {
		String hits = String.format(TEST_METHOD_INFO, "End", testedObject.getClass().getName(), testedMethod.getName(),
				Thread.currentThread().getId());
		try {
			getTestListener().afterMethod(testedObject, testedMethod);
			return null;
		} catch (Throwable e) {
			e.printStackTrace();
			return e;
		} finally {
			MessageHelper.info(hits + "\n");
		}
	}

	/**
	 * 执行teardownClass的事件
	 * 
	 * @param testedObject
	 *            测试对象
	 * @return 事件异常
	 */
	public static Throwable executeAfterClassEvents(Object testedObject) {
		String clazName = testedObject.getClass().getName();
		String hits = String.format(TEST_CLAZZ_INFO, "End", clazName, Thread.currentThread().getId());
		try {
			getTestListener().afterClass(testedObject);
			return null;
		} catch (Throwable e) {
			e.printStackTrace();
			return e;
		} finally {
			MessageHelper.info(hits + "\n");
		}
	}

	/**
	 * @return The jTester test listener
	 */
	private static TestListener getTestListener() {
		return CoreModule.getTestListener();
	}
}
