package org.jtester.junit.demo.features;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * junit并行测试的示例
 * 
 * @author darui.wudr
 * 
 */
public class ParallelTestDemo {
	public static class Example {
		@Test
		public void one() throws InterruptedException {
			Thread.sleep(1000);
		}

		@Test
		public void two() throws InterruptedException {
			Thread.sleep(1000);
		}
	}

	/**
	 * 并行运行Example中的测试
	 */
	@Test
	public void testsRunInParallel() {
		long start = System.currentTimeMillis();
		Result result = JUnitCore.runClasses(ParallelComputer.methods(), Example.class);
		assertTrue(result.wasSuccessful());
		long end = System.currentTimeMillis();
		boolean between = end - start >= 1000 && end - start <= 2000;
		System.out.println(end - start);
		assertTrue(between);
	}
}
