package org.jtester.module.tracer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.jtester.json.JSON;
import org.jtester.json.helper.JSONFeature;
import org.jtester.module.core.TestContext;
import org.jtester.tools.commons.DateHelper;
import org.jtester.tools.commons.ResourceHelper;

@SuppressWarnings("rawtypes")
public abstract class TracerLogger {
	protected final String method;

	public TracerLogger() {
		this.method = TestContext.currTestedMethodName();
	}

	/**
	 * 记录调用方法的参数值
	 * 
	 * @param claz
	 * @param method
	 * @param values
	 */
	public abstract void writerMethodInputInfo(Class claz, String method, Object[] values);

	/**
	 * 记录方法的异常值
	 * 
	 * @param claz
	 * @param method
	 * @param exception
	 */
	public abstract void writerMethodException(Class claz, String method, Throwable exception);

	/**
	 * 记录方法的返回值
	 * 
	 * @param claz
	 * @param method
	 * @param result
	 */
	public abstract void writerMethodReturnValue(Class claz, String method, Object result);

	/**
	 * 记录sql语句和它的返回值
	 * 
	 * @param sql
	 * @param result
	 */
	public abstract void writerSqlStatement(String sql, Object result);

	/**
	 * 关闭当前记录
	 */
	public abstract void close();

	String toJSON(Object o) {
		try {
			String json = JSON.toJSON(o, JSONFeature.UnMarkClassFlag, JSONFeature.SkipNullValue,
					JSONFeature.SkipNullValue);
			return json;
		} catch (Throwable e) {
			return "toJSON error:" + e.getMessage();
		}
	}

	static Writer getWriter(String surfix) throws IOException {
		String basedir = TracerHelper.tracerFileDir();
		String method = TestContext.currTestedMethodName();
		String file = basedir + "/" + getFile(method, surfix);
		File xmlFile = new File(file);
		ResourceHelper.mkFileParentDir(xmlFile);
		BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile, false));
		return writer;
	}

	static String getFile(String method, String surfix) {
		StringBuffer file = new StringBuffer();
		file.append(method.replace('.', '/'));
		file.append(".");
		file.append(DateHelper.currDateTimeStr("yyMMdd.HHmmss."));
		file.append(surfix);
		return file.toString();
	}

	public static TracerLogger instance(boolean logInTxt) {
		if (logInTxt) {
			return new XmlFileTracerLogger();
		} else {
			return new DbTracerLogger();
		}
	}
}
