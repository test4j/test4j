package org.jtester.module.tracer;

@SuppressWarnings({ "rawtypes" })
public class TracerManager {
	/**
	 * 序列记录器
	 */
	static ThreadLocal<TracerLogger> tracerFile = new ThreadLocal<TracerLogger>();

	/**
	 * 是否挂起jdbc记录
	 */
	static ThreadLocal<Boolean> suspendJdbcTracer = new ThreadLocal<Boolean>();

	private static final boolean logInTxt = TracerHelper.tracerLogInFile();

	/**
	 * 开始记录spring和jdbc跟踪信息
	 */
	public static void startTracer() {
		boolean tracerEnabled = TracerHelper.doesTracerEnabled();
		suspendJdbcTracer.set(false);
		if (tracerEnabled == false) {
			tracerFile.remove();
			return;
		}
		tracerFile.set(TracerLogger.instance(logInTxt));
	}

	/**
	 * 结束输出spring bean和jdbc跟踪信息
	 */
	public static void endTracer() {
		TracerLogger logger = tracerFile.get();
		if (logger != null) {
			logger.close();
		}
		tracerFile.remove();
	}

	/**
	 * 挂起jdbc跟踪
	 */
	public static void suspendJDBC() {
		suspendJdbcTracer.set(true);
	}

	/**
	 * 继续jdbc跟踪
	 */
	public static void continueJDBC() {
		suspendJdbcTracer.set(false);
	}

	/**
	 * 记录调用方法的入参
	 * 
	 * @param beanClazz
	 * @param method
	 * @param paras
	 */
	public static void traceBeanInputs(Class beanClazz, String method, Object[] paras) {
		TracerLogger logger = tracerFile.get();
		if (logger == null) {
			return;
		}
		logger.writerMethodInputInfo(beanClazz, method, paras);
	}

	/**
	 * 记录jdbc的sql语句
	 * 
	 * @param sql
	 */
	public static void traceJdbcStatement(String sql) {
		Boolean jdbcSuspend = suspendJdbcTracer.get();
		TracerLogger logger = tracerFile.get();
		if (jdbcSuspend == null || jdbcSuspend == true || logger == null) {
			return;
		} else {
			logger.writerSqlStatement(sql, null);
		}
	}

	/**
	 * 记录调用方法的返回值
	 * 
	 * @param beanClazz
	 * @param method
	 * @param result
	 */
	public static void traceBeanReturn(Class beanClazz, String method, Object result) {
		TracerLogger logger = tracerFile.get();
		if (logger == null) {
			return;
		}
		logger.writerMethodReturnValue(beanClazz, method, result);
	}

	/**
	 * 记录调用方法抛出的异常
	 * 
	 * @param beanClazz
	 * @param method
	 * @param e
	 */
	public static void traceBeanException(Class beanClazz, String method, Throwable e) {
		TracerLogger logger = tracerFile.get();
		if (logger == null) {
			return;
		}
		logger.writerMethodException(beanClazz, method, e);
	}
}
