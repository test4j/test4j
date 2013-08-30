package org.jtester.testng.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class TestNgUtil {
	/**
	 * 运行testng的单个测试方法
	 * 
	 * @param clazz
	 *            测试类
	 * @param method
	 *            方法名称
	 * @param isThrowException
	 *            是否抛出测试异常消息
	 * @return
	 */
	public static boolean run(String clazz, String method, boolean isThrowException) {
		TestNG tng = getTestRunner(clazz, method, isThrowException);
		TestListenerAdapter listener = new TestListenerAdapter();
		tng.addListener(listener);
		tng.run();
		int success = listener.getPassedTests().size();
		int failure = listener.getFailedTests().size();
		if (isThrowException) {
			for (ITestResult rt : listener.getFailedTests()) {
				throw new RuntimeException(rt.getThrowable());
			}
		}
		return success == 1 && failure == 0;
	}

	/**
	 * 构造单个方法的testng runner
	 * 
	 * @param clazz
	 * @param method
	 * @param isThrowException
	 * @return
	 */
	public static TestNG getTestRunner(String clazz, String method, boolean isThrowException) {
		TestNG tng = new TestNG();
		XmlSuite suite = new XmlSuite();
		XmlTest test = new XmlTest(suite);
		test.setName("run testng");
		XmlClass xmlClazz = new XmlClass(clazz);
		try {
			/**
			 * 为了兼容testng 5.11(以下版)和5.12(以上版)，采用了try catch
			 */
			List includes = xmlClazz.getIncludedMethods();
			try {
				// 下列代码其实就是includes.add(new XmlInclude(method)); <br>
				// 采用反射构造是为了在testNg5.11下也可以编译通过
				Class xmlInclude = Class.forName("org.testng.xml.XmlInclude");
				Constructor constructor = xmlInclude.getConstructor(String.class);
				includes.add(constructor.newInstance(method));
			} catch (Throwable e) {
				includes.add(method);
			}
			xmlClazz.getExcludedMethods().add(method + ".+");
		} catch (Throwable e) {

		}
		test.getXmlClasses().add(xmlClazz);

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		tng.setXmlSuites(suites);

		return tng;
	}

	/**
	 * 判断一个方法是否为单元测试方法<br>
	 * <br>
	 * <br>
	 * o method前面直接标注了@Test <br>
	 * o 否则，如果class未标注@Test或者方法为非public类型或静态方法，则不是test method<br>
	 * o 否则，如果class即标注了@Test + public非静态方法 + 且没有其它标注，则是test method<br>
	 * o 如果有标注，则判断标注的用途
	 * 
	 * @param testClass
	 * @param method
	 * @return
	 */
	public static boolean isTestMethod(Class testClass, Method method) {
		boolean methodHasTestAnnotation = method.isAnnotationPresent(Test.class);
		if (methodHasTestAnnotation) {
			return true;
		}
		boolean clazHasTestAnnotation = testClass.isAnnotationPresent(Test.class);
		Class superClaz = testClass.getSuperclass();
		while (superClaz != Object.class && clazHasTestAnnotation == false) {
			clazHasTestAnnotation = superClaz.isAnnotationPresent(Test.class);
			superClaz = superClaz.getSuperclass();
		}
		int modifier = method.getModifiers();
		if (clazHasTestAnnotation == false || Modifier.isPublic(modifier) == false) {
			return false;
		}

		Annotation[] annotations = method.getDeclaredAnnotations();
		if (annotations == null || annotations.length == 0) {
			return true;
		}
		for (Annotation annotation : annotations) {
			Class claz = annotation.annotationType();
			if (NOT_TEST_ANNOTATIONS.contains(claz)) {
				return false;
			}
		}
		return true;
	}

	private final static Set<Class> NOT_TEST_ANNOTATIONS = new HashSet<Class>() {
		private static final long serialVersionUID = 7752423831360820098L;
		{
			this.add(BeforeSuite.class);
			this.add(BeforeTest.class);
			this.add(BeforeGroups.class);
			this.add(BeforeClass.class);
			this.add(BeforeMethod.class);

			this.add(AfterSuite.class);
			this.add(AfterTest.class);
			this.add(AfterGroups.class);
			this.add(AfterClass.class);
			this.add(AfterMethod.class);

			this.add(DataProvider.class);
			this.add(Factory.class);
		}
	};
}
