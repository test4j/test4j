package org.test4j.junit.extend;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.test4j.junit.extend.demo.InterceptorRunner;
import org.test4j.junit.extend.demo.SampleLoggingInterceptor;
import org.test4j.junit.extend.demo.InterceptorRunner.InterceptorClasses;

@RunWith(InterceptorRunner.class)
@InterceptorClasses(value = { SampleLoggingInterceptor.class })
public class TestCustomRunnerWithLoggingInterceptor {
	@Before
	public void setUp() throws InterruptedException {
		System.out.println("Real before");
		Thread.sleep(100);
	}

	@Test
	public void testDummy() throws InterruptedException {
		Thread.sleep(500);
		assertTrue(true);
		System.out.println("Some text for test purpose");
	}

	@After
	public void tearDown() throws InterruptedException {
		Thread.sleep(100);
		System.out.println("Real after");
	}
}
