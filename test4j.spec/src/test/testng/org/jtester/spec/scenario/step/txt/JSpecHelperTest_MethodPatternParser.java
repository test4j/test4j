package org.jtester.spec.scenario.step.txt;

import java.lang.reflect.Method;

import org.jtester.spec.annotations.Named;
import org.jtester.spec.scenario.step.txt.ParameterParser;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

public class JSpecHelperTest_MethodPatternParser extends JTester {
	@Test
	public void testGetPatternAsStringFromMethod() throws Exception {
		String methodname = "hasNameAnno";
		Method method = NameMethodClaz.class.getMethod(methodname, String.class);
		String pattern = ParameterParser.getPatternAsStringFromMethod(method);
		want.string(pattern).isEqualTo(methodname + " $value");
	}

	@Test
	public void testGetPatternAsStringFromMethod_NoAnnotations() throws Exception {
		String methodname = "noNamedAnno";
		Method method = NameMethodClaz.class.getMethod(methodname, String.class, String.class);
		String pattern = ParameterParser.getPatternAsStringFromMethod(method);
		want.string(pattern).isEqualTo(methodname + " $1 $2");
	}

	@Test
	public void testGetPatternAsStringFromMethod_mixNamedAnno() throws Exception {
		String methodname = "mixNamedAnno";
		Method method = NameMethodClaz.class.getMethod(methodname, String.class, String.class);
		String pattern = ParameterParser.getPatternAsStringFromMethod(method);
		want.string(pattern).isEqualTo(methodname + " $1 $value2");
	}
}

class NameMethodClaz {
	public void hasNameAnno(@Named("value") String value) {
	}

	public void noNamedAnno(String value1, String value2) {
	}

	public void mixNamedAnno(String value1, @Named("value2") String value2) {
	}
}
