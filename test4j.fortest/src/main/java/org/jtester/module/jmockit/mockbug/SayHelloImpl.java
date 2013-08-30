package org.jtester.module.jmockit.mockbug;

import org.jtester.module.core.utility.MessageHelper;

public class SayHelloImpl {

	public SayHelloImpl() {
		MessageHelper.info("init log");
	}

	public String sayHello() {
		MessageHelper.info("如果@Mock 一个实现类的第一次运行的时候,静态变量会被置为null,此处抛出NullPointerException");
		return "say hello:" + getName();
	}

	private String getName() {
		return "darui.wu";
	}
}
