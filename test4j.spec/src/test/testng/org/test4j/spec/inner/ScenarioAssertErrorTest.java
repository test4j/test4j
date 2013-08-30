package org.test4j.spec.inner;

import java.util.Arrays;
import java.util.List;

import mockit.Mock;

import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.spec.exceptions.ScenarioAssertError;
import org.test4j.spec.scenario.JSpecScenario;
import org.test4j.spec.scenario.TxtJSpecScenario;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

public class ScenarioAssertErrorTest extends Test4J {

	@Test
	public void testGetError() {
		Exception e1 = new RuntimeException("runtime exception test");
		Exception e2 = new Exception("checked exception test");
		Throwable e3 = new AssertionError("assert error test");
		List<Throwable> list = Arrays.asList(e1, e2, e3);
		JSpecScenario scenario = reflector.newInstance(TxtJSpecScenario.class);
		new MockUp<JSpecScenario>() {
			@Mock
			public String toString() {
				return "my scenario";
			}
		};
		Throwable e = new ScenarioAssertError(scenario, list);
		String msg = e.getMessage();
		want.string(msg).contains(new String[] { "runtime exception test",// <br>
				"checked exception test",// <br>
				"assert error test",// <br>
				"my scenario throw errors at:" }, StringMode.IgnoreSpace);
	}
}
