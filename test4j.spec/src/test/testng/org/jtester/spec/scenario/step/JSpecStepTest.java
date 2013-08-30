package org.jtester.spec.scenario.step;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jtester.spec.inner.StepType;
import org.jtester.spec.scenario.step.JSpecStep;
import org.jtester.spec.scenario.step.txt.LineType;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "serial" })
public class JSpecStepTest extends JTester {

	@Test(groups = "jspec", dataProvider = "dataForJSpecStep")
	public void testJSpecStep(String line, final StepType type, final String methodname, final boolean skip) {
		LineType lineType = LineType.getLineType(line);
		JSpecStep step = new TxtJSpecStep("test", line, lineType);
		want.object(step).propertyEqMap(new DataMap() {
			{
				this.put("type", type);
				this.put("method", methodname);
				this.put("isSkip", skip);
			}
		});
	}

	@DataProvider
	public Iterator dataForJSpecStep() {
		return new DataIterator() {
			{
				data("Given test", StepType.Given, "test", false);

				data("Given test", StepType.Given, "test", false);

				data("SkipGiven test", StepType.Given, "test", true);

				data("SkipGiven test method", StepType.Given, "testMethod", true);

				data("\tWhen test method", StepType.When, "testMethod", false);

				data(" Then\ti am a display", StepType.Then, "iAmADisplay", false);
			}
		};
	}

	@Test(dataProvider = "dataForParseStepType")
	public void testParseStepType(String line) {
		LineType lineType = LineType.getLineType(line);
		try {
			new TxtJSpecStep("test", line, lineType);
			want.fail();
		} catch (RuntimeException e) {
			String message = e.getMessage();
			want.string(message).contains("illegal step type");
		}
	}

	@DataProvider
	public Iterator dataForParseStepType() {
		return new DataIterator() {
			{
				data("Giventest");
				data("SkipGiventest");
				data("Whentest method");
				data("SkipWhentest method");
				data("Thentest method");
				data("SkipThentest method");
			}
		};
	}

	@Test
	public void testGetArguments_Error() {
		JSpecStep step = new TxtJSpecStep("test", "do test", LineType.Given);
		step.parseStep("【userName=darui.wudr】【password=ddd】", null);
		List<Type> types = new ArrayList<Type>() {
			{
				add(String.class);
				add(String.class);
			}
		};
		try {
			step.getArguments(Arrays.asList("userName", "userPass"), types);
			want.fail();
		} catch (Exception e) {
			String error = e.getMessage();
			want.string(error).isEqualTo(
					"can't find parameter userPass, the existed parameters are [userName,password].");
		}
	}

	@Test
	public void testGetArguments() {
		JSpecStep step = new TxtJSpecStep("test", "do test", LineType.Given);
		step.parseStep("【userName=darui.wudr】【userPass='ddd'】", null);
		List<Type> types = new ArrayList<Type>() {
			{
				add(String.class);
				add(String.class);
			}
		};
		Object[] values = step.getArguments(Arrays.asList("userName", "userPass"), types);
		want.array(values).reflectionEq(new String[] { "darui.wudr", "ddd" });
	}
}
