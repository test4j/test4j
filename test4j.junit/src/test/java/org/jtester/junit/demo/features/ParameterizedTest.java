package org.jtester.junit.demo.features;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.jtester.junit.extend.model.Calculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * junit本身参数化测试示例
 * 
 * @author darui.wudr
 * 
 */
@RunWith(value = Parameterized.class)
public class ParameterizedTest {
	private double expected;
	private double valueOne;
	private double valueTwo;

	@Parameters
	public static Collection<Integer[]> getTestParameters() {
		return Arrays.asList(new Integer[][] { { 2, 1, 1 },// <br>
				{ 3, 2, 1 },// <br>
				{ 4, 3, 1 }, });
	}

	public ParameterizedTest(double expected, double valueOne, double valueTwo) {
		this.expected = expected;
		this.valueOne = valueOne;
		this.valueTwo = valueTwo;
	}

	@Test
	public void sum() {
		Calculator calc = new Calculator();
		assertEquals(expected, calc.add(valueOne, valueTwo), 0);
	}
}
