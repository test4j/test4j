package org.jtester.testng;

import org.jtester.module.core.utility.MessageHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class PrintFailureMethodListener implements ITestListener {

	public void onFinish(ITestContext context) {
	}

	public void onStart(ITestContext context) {
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	public void onTestFailure(ITestResult result) {
		Throwable e = result.getThrowable();
		if (e != null) {
			e.printStackTrace();
		}
		Object[] paras = result.getParameters();
		if (paras == null) {
			return;
		}
		StringBuffer des = new StringBuffer("parameters:");
		boolean first = true;
		for (Object para : paras) {
			if (first) {
				first = false;
			} else {
				des.append(";");
			}
			des.append(para);
		}
		MessageHelper.info(des.toString());
	}

	public void onTestSkipped(ITestResult result) {
	}

	public void onTestStart(ITestResult result) {
	}

	public void onTestSuccess(ITestResult result) {
	}
}
