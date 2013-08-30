package org.jtester.module.dbfit.utility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.DbFit.AUTO;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.ResourceHelper;

/**
 * 用于自动查找dbfit文件
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class AutoFindDbFit {
	/**
	 * 测试方法默认的DbFit when wiki文件<br>
	 * ${class} 是测试类名称<br>
	 * ${method} 测试方法名称
	 */
	public final static String METHOD_WHEN_WIKI = "data/%s/%s.when.wiki";

	/**
	 * 测试方法默认的DbFit when sql文件<br>
	 * ${class} 是测试类名称<br>
	 * ${method} 测试方法名称
	 */
	public final static String METHOD_WHEN_SQL = "data/%s/%s.when.sql";

	/**
	 * 测试方法默认的DbFit then wiki文件<br>
	 * ${class} 是测试类名称<br>
	 * ${method} 测试方法名称
	 */
	public final static String METHOD_THEN_WIKI = "data/%s/%s.then.wiki";

	/**
	 * 测试方法默认的DbFit then sql文件<br>
	 * ${class} 是测试类名称<br>
	 * ${method} 测试方法名称
	 */
	public final static String METHOD_THEN_SQL = "data/%s/%s.then.sql";

	/**
	 * 测试类默认的DbFit when wiki文件<br>
	 * ${class} 是测试类名称<br>
	 */
	public final static String CLAZZ_WHEN_WIKI = "data/%s.wiki";

	/**
	 * 测试类默认的DbFit when sql文件<br>
	 * ${class} 是测试类名称<br>
	 */
	public final static String CLAZZ_WHEN_SQL = "data/%s.sql";

	/**
	 * 查找class级别所有的DbFit when文件
	 * 
	 * @param testClazz
	 * @return
	 */
	public static String[] autoFindClassWhen(Class testClazz) {
		DbFit dbFit = AnnotationHelper.getClassLevelAnnotation(DbFit.class, testClazz);
		List<String> whens = getFiles(dbFit == null ? null : dbFit.when());
		if (dbFit != null && dbFit.auto() == AUTO.UN_AUTO) {
			return whens.toArray(new String[0]);
		} else {
			String wiki = String.format(CLAZZ_WHEN_WIKI, testClazz.getSimpleName());
			if (whens.contains(wiki) == false && ResourceHelper.isResourceExists(testClazz, wiki)) {
				whens.add(wiki);
			}
			String sql = String.format(CLAZZ_WHEN_SQL, testClazz.getSimpleName());
			if (whens.contains(sql) == false && ResourceHelper.isResourceExists(testClazz, sql)) {
				whens.add(sql);
			}
			return whens.toArray(new String[0]);
		}
	}

	/**
	 * 查找test method级别所有的when文件<br>
	 * 
	 * @param testClazz
	 * @param testMethod
	 * @return
	 */
	public static String[] autoFindMethodWhen(Class testClazz, Method testMethod) {
		boolean isAutoFind = isAutoFind(testClazz, testMethod);

		DbFit dbFit = testMethod.getAnnotation(DbFit.class);
		List<String> whens = getFiles(dbFit == null ? null : dbFit.when());
		if (isAutoFind == false) {
			return whens.toArray(new String[0]);
		} else {
			String wiki = String.format(METHOD_WHEN_WIKI, testClazz.getSimpleName(), testMethod.getName());
			if (whens.contains(wiki) == false && ResourceHelper.isResourceExists(testClazz, wiki)) {
				whens.add(wiki);
			}
			String sql = String.format(METHOD_WHEN_SQL, testClazz.getSimpleName(), testMethod.getName());
			if (whens.contains(sql) == false && ResourceHelper.isResourceExists(testClazz, sql)) {
				whens.add(sql);
			}
			return whens.toArray(new String[0]);
		}
	}

	/**
	 * 查找test method级别所有的then文件
	 * 
	 * @param testClazz
	 * @param testMethod
	 * @return
	 */
	public static String[] autoFindMethodThen(Class testClazz, Method testMethod) {
		boolean isAutoFind = isAutoFind(testClazz, testMethod);

		DbFit dbFit = testMethod.getAnnotation(DbFit.class);
		List<String> thens = getFiles(dbFit == null ? null : dbFit.then());

		if (isAutoFind == false) {
			return thens.toArray(new String[0]);
		} else {
			String wiki = String.format(METHOD_THEN_WIKI, testClazz.getSimpleName(), testMethod.getName());
			if (thens.contains(wiki) == false && ResourceHelper.isResourceExists(testClazz, wiki)) {
				thens.add(wiki);
			}
			String sql = String.format(METHOD_THEN_SQL, testClazz.getSimpleName(), testMethod.getName());
			if (thens.contains(sql) == false && ResourceHelper.isResourceExists(testClazz, sql)) {
				thens.add(sql);
			}
			return thens.toArray(new String[0]);
		}
	}

	/**
	 * 判断一个测试方法根据规则自动查找wiki文件
	 * 
	 * @param testClazz
	 *            测试类
	 * @param testMethod
	 *            测试方法
	 * @return
	 */
	private static boolean isAutoFind(Class testClazz, Method testMethod) {
		DbFit methodDbFit = testMethod.getAnnotation(DbFit.class);
		if (methodDbFit != null && methodDbFit.auto() != AUTO.DEFAULT) {
			return methodDbFit.auto() == AUTO.AUTO;
		}

		DbFit clazzDbFit = AnnotationHelper.getClassLevelAnnotation(DbFit.class, testClazz);
		if (clazzDbFit == null) {
			return true;
		} else {
			return !(clazzDbFit.auto() == AUTO.UN_AUTO);
		}
	}

	private static List<String> getFiles(String[] files) {
		List<String> list = new ArrayList<String>();
		if (files == null || files.length == 0) {
			return list;
		}
		for (String file : files) {
			file = file.replace('\\', '/');
			list.add(file);
		}
		return list;
	}
}
