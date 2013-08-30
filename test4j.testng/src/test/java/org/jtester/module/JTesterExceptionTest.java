package org.jtester.module;

import org.jtester.testng.JTester;
import org.test4j.module.JTesterException;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class JTesterExceptionTest extends JTester {
	@Test(expectedExceptions = { JTesterException.class })
	public void exception1() {
		throw new JTesterException("message");
	}

	@Test(expectedExceptions = { JTesterException.class })
	public void exception2() {
		throw new JTesterException(new Exception("message"));
	}

	@Test(expectedExceptions = { JTesterException.class })
	public void exception3() {
		throw new JTesterException("message", new Exception("message"));
	}
}
