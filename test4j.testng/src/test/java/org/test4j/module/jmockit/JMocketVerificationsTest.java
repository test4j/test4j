package org.test4j.module.jmockit;

import mockit.NonStrict;

import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.jmockit.extend.JMocketVerifications;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@Test
public class JMocketVerificationsTest extends JTester {

	@NonStrict
	Hello hello1;

	public void testVerifyApi() {
		Hello hello = new Hello();
		hello.sayHello("darui.wu");

		new JMocketVerifications() {
			{
				hello1.sayHello(with("1", the.string().contains("wu")));
				times = 1;
			}
		};
	}

	public static class Hello {
		void sayHello(String name) {
			MessageHelper.info("hello world, " + name);
		}
	}
}
