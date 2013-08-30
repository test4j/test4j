package org.jtester.testng.jmockit;

import java.util.ArrayList;

import mockit.Mocked;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserService;
import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "mock-demo" })
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
		"org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class MockBeanTest extends JTester {
	@SpringBeanByName
	private UserService userService;

	@SpringBeanFrom
	@Mocked
	private UserDao userDao;

	@Transactional(TransactionMode.DISABLED)
	public void paySalary_ThrowRuntimeException_WithSpringWrapped() {
		new Expectations() {
			{
				when(userDao.findUserByPostcode("310000")).thenThrows(new RuntimeException("test"));
			}
		};
		try {
			this.userService.paySalary("310000");
		} catch (Throwable e) {
			e.printStackTrace();
			String message = e.getMessage();
			want.string(message).contains("test");
		}
	}

	public void paySalary() {
		new Expectations() {
			{
				when(userDao.findUserByPostcode("310000")).thenReturn((new ArrayList<User>() {
					private static final long serialVersionUID = -2799578129563837839L;
					{
						this.add(new User(1, 1000d));
						this.add(new User(2, 1500d));
						this.add(new User(2, 1800d));
					}
				}));
			}
		};
		double total = this.userService.paySalary("310000");
		want.number(total).isEqualTo(4300d);
	}

	public void paySalary2() {
		new Expectations() {
			{
				when(userDao.findUserByPostcode("310000")).thenReturn(new ArrayList<User>() {
					private static final long serialVersionUID = -2799578129563837839L;
					{
						this.add(new User(1, 1000d));
						this.add(new User(2, 1500d));
						this.add(new User(2, 1800d));
					}
				});
			}
		};

		double total = this.userService.paySalary("310000");
		want.number(total).isEqualTo(4300d);
	}
}
