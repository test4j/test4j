package org.test4j.module.dbfit.utility;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.test4j.module.Test4JException;
import org.test4j.tools.reflector.MethodAccessor;

import fitlibrary.batch.testRun.FitLibraryTestEngine;
import fitlibrary.batch.trinidad.TestDescriptor;
import fitlibrary.batch.trinidad.TestResult;

public class Test4JFitLibraryTestEngine extends FitLibraryTestEngine {
    private final static MethodAccessor<TestResult> runTestMethod = new MethodAccessor<TestResult>(
                                                                          FitLibraryTestEngine.class, "runTest",
                                                                          TestDescriptor.class, OutputStream.class,
                                                                          OutputStream.class);

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
            throw new Test4JException("Unable to invoke method[runTest].", e);
        }
    }
}
