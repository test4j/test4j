package org.jtester.module.dbfit;

public class DbFitTestedContext {
	/**
	 * 当前进程dbFit运行容器
	 */
	private static ThreadLocal<RunIn> localRunIn = new ThreadLocal<RunIn>();

	private static RunIn currRunIn = RunIn.TestCase;

	/**
	 * 设置DbFit是运行于那个容器内<br>
	 * <br>
	 * o TestNG和JUnit的测试类中<br>
	 * o FitNesse Fixture中<br>
	 * 
	 * @param runIn
	 */
	public static void setRunIn(RunIn runIn) {
		localRunIn.set(runIn);
		currRunIn = runIn;
	}

	/**
	 * 返回DbFit是运行于那个容器内<br>
	 * <br>
	 * o TestNG和JUnit的测试类中<br>
	 * o FitNesse Fixture中<br>
	 * 
	 * @param runIn
	 */
	public static RunIn getRunIn() {
		RunIn local = localRunIn.get();
		if (local == null) {
			local = currRunIn;
		}
		return local;
	}

	/**
	 * dbFit的运行容器
	 * 
	 * @author darui.wudr
	 * 
	 */
	public static enum RunIn {
		TestCase, FitNesse;
	}
}
