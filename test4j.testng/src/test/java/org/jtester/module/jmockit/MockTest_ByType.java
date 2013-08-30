package org.jtester.module.jmockit;

import mockit.Mocked;

import org.jtester.fortest.beans.ISpeak;
import org.jtester.fortest.beans.Person;
import org.jtester.module.inject.annotations.Inject;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class MockTest_ByType extends JTester {
	// @TestedObject
	private Person person = new Person();

	@Mocked
	@Inject(targets = "person")
	private ISpeak speak;

	public void sayHello() {
		new Expectations() {
			{
				speak.say(the.string().contains("darui.wu").wanted());
			}
		};
		person.sayHelloTo("darui.wu");
	}

	public void sayHello_2() {
		new Expectations() {
			{
				speak.say(the.string().contains("darui.wu").wanted());
			}
		};
		person.sayHelloTo("darui.wu");
	}

	public void sayHello_3() {
		new Expectations() {
			{
				speak.say((String)with(the.string().contains("darui.wu")));
			}
		};
		person.sayHelloTo("darui.wu");
	}
}
