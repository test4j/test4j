package org.jtester.testng.spring;

import java.util.List;

import mockit.NonStrict;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserDaoImpl;
import org.jtester.fortest.service.UserService;
import org.jtester.module.database.IDatabase;
import org.jtester.module.spring.ISpring;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = @BeanMap(intf = "**.UserAnotherDao", impl = "**.UserAnotherDaoImpl"))
@Test(groups = "jtester")
@SuppressWarnings("unused")
public class JTesterClassPathXmlApplicationContextTest_MultiThread extends JTester implements IDatabase, ISpring {
	@SpringBeanFrom
	@NonStrict
	private UserService userService;

	@SpringBeanByName(claz = UserDaoImpl.class)
	private UserDao userDao;

	int count = 0;

	/**
	 * 测试在多线程中获取mocked bean
	 * 
	 * @throws InterruptedException
	 */
	@Test
	// (groups = "for-test")
	public void testGetBean() throws InterruptedException {
		count = 0;
		new Expectations() {
			{
				userService.findAllUser();
				result = new Delegate() {
					public List<User> findAllUser() {
						count++;
						return null;
					}
				};
			}
		};
		userService.findAllUser();
		for (int loop = 0; loop < 10; loop++) {
			Runnable runnable = new Runnable() {

				public void run() {
					UserService userService = spring.getBean("userService");
					userService.findAllUser();
				}
			};

			new Thread(runnable).start();
		}
		Thread.sleep(200);
		want.number(count).isEqualTo(11);
	}
}
