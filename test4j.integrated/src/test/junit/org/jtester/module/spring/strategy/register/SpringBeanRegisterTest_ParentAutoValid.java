package org.jtester.module.spring.strategy.register;

import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.junit.Test;

/**
 * 验证父类的注入是否对子类有效
 * 
 * @author darui.wudr
 * 
 */
public class SpringBeanRegisterTest_ParentAutoValid extends RegisterDynamicBeanTest {
	@Test
	public void checkUserAnotherDao() {
		Object userDao = spring.getBean("userAnotherDao");
		want.object(userDao).notNull();
	}

	@Test
	public void checkUserServiceBean() {
		want.object(userService).notNull();
		Object o = spring.getBean("userService");
		want.object(o).same(userService);
	}

	/**
	 * 测试多线程状态下的测试事务管理<br>
	 * 注：当threadPoolSize值设的较大时，jmockit有时会抛出NullPointer错误
	 */
	@Transactional(TransactionMode.COMMIT)
	@Test
	// (invocationCount = 12, threadPoolSize = 3)
	public void getSpringBean_multiThread() {
		want.object(userService).notNull();
		Object o = spring.getBean("userAnotherDao");
		want.object(o).same(userAnotherDao);

		Object userDao = spring.getBean("userDao");
		want.object(userDao).notNull();
	}
}
