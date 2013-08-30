package org.jtester.tools.commons;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.jtester.module.JTesterException;

/**
 * 获得一个类（方法）的测试类（方法）的工具
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class MethodFinder {
	/**
	 * 获得claz的所有测试类
	 * 
	 * @param claz
	 * @return
	 */
	public static List<String> findTestClaz(Class claz) {
		List<String> clazzes = ClazzFinder.findClazz(claz);
		String classname = claz.getName();
		List<String> tests = MethodFinder.filterClaz(clazzes, classname);

		return tests;
	}

	/**
	 * 获得claz的所有测试类
	 * 
	 * @param claz
	 * @return
	 */
	public static List<String> findTestClaz(String claz) {
		List<String> clazzes = ClazzFinder.findClazz(claz);
		String classname = claz.substring(claz.lastIndexOf("."));
		List<String> tests = MethodFinder.filterClaz(clazzes, classname);

		return tests;
	}

	private static List<String> filterClaz(List<String> clazzes, String classname) {
		List<String> tests = new LinkedList<String>();
		if (clazzes == null) {
			return tests;
		}
		for (String _claz : clazzes) {
			if (_claz.contains("$") || _claz.equals(classname)) {
				continue;
			}
			if (_claz.contains(classname)) {
				tests.add(_claz);
			}
		}
		return tests;
	}

	/**
	 * 获得method的所有测试方法
	 * 
	 * @param claz
	 * @param method
	 * @return
	 */
	public static List<String> findTestMethod(Class claz, Method method) {
		return MethodFinder.findTestMethod(claz.getName(), method.getName());
	}

	/**
	 * 获得method的所有测试方法
	 * 
	 * @param claz
	 * @param method
	 * @return
	 */
	public static List<String> findTestMethod(Class claz, String methodname) {
		List<String> clazzes = MethodFinder.findTestClaz(claz);
		List<String> testmethods = new LinkedList<String>();
		for (String classname : clazzes) {
			Class clazz = null;
			try {
				clazz = Class.forName(classname);
			} catch (ClassNotFoundException e) {
				throw new JTesterException(e);
			}
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.getName().indexOf(methodname) == 0 || method.getName().indexOf("test_" + methodname) == 0) {
					testmethods.add(classname + "." + method.getName());
				}
			}
		}

		return testmethods;
	}

	/**
	 * 获得method的所有测试方法
	 * 
	 * @param claz
	 * @param method
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<String> findTestMethod(String classname, String methodname) {
		try {
			Class claz = Class.forName(classname);
			return MethodFinder.findTestMethod(claz, methodname);
		} catch (ClassNotFoundException e) {
			throw new JTesterException(e);
		}
	}
}
