package org.jtester.module.dbfit.utility;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.jtester.module.JTesterException;
import org.jtester.tools.reflector.MethodAccessor;

import fitlibrary.batch.testRun.FitLibraryTestEngine;
import fitlibrary.batch.trinidad.TestDescriptor;
import fitlibrary.batch.trinidad.TestResult;

public class JTesterFitLibraryTestEngine extends FitLibraryTestEngine {
	private final static MethodAccessor<TestResult> runTestMethod = new MethodAccessor<TestResult>(
			FitLibraryTestEngine.class, "runTest", TestDescriptor.class, OutputStream.class, OutputStream.class);

	/**
	 * 禁止把log输出和system输出重定向到结果页面
	 */
	@Override
	public TestResult runTest(TestDescriptor test) {
		OutputStream tempOut = new ByteArrayOutputStream();
		OutputStream tempErr = new ByteArrayOutputStream();
		try {
			TestResult o = runTestMethod.invoke(this, new Object[] { test, tempOut, tempErr });
			return o;
		} catch (Exception e) {
			throw new JTesterException("Unable to invoke method[runTest].", e);
		}
	}
}
