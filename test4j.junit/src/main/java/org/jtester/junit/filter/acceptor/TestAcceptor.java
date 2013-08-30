package org.jtester.junit.filter.acceptor;

/**
 * 判断一个测试类是否运行被运行
 * 
 * @author darui.wudr
 * 
 */
public interface TestAcceptor {
	/**
	 * 测试类类型是否正确(junt4,junit38,suite test)
	 * 
	 * @param clazz
	 * @return
	 */
	boolean isCorrectTestType(Class<?> clazz);

	/**
	 * 测试类名称是否符合指定的运行规则
	 * 
	 * @param className
	 * @return
	 */
	boolean isAcceptedByPatterns(String className);

	/**
	 * 是否是可实例化的类以及拥有指定的基类
	 * 
	 * @param clazz
	 * @return
	 */
	boolean isCorrectClazType(Class<?> clazz);

	/**
	 * 是否运行内部类运行
	 * 
	 * @return
	 */
	boolean acceptInnerClass();

	/**
	 * 是否在jar包中查找测试
	 * 
	 * @return
	 */
	boolean searchInJars();

	/**
	 * 默认测试接收器，接收所有的测试类
	 * 
	 * @author darui.wudr
	 * 
	 */
	public static class AllTestAcceptor implements TestAcceptor {

		public boolean isAcceptedByPatterns(String className) {
			return true;
		}

		public boolean acceptInnerClass() {
			return true;
		}

		public boolean isCorrectTestType(Class<?> clazz) {
			return true;
		}

		public boolean searchInJars() {
			return true;
		}

		public boolean isCorrectClazType(Class<?> clazz) {
			return true;
		}
	}
}