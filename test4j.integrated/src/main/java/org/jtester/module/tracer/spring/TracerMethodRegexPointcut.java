package org.jtester.module.tracer.spring;

import java.lang.reflect.Modifier;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

@SuppressWarnings("rawtypes")
public class TracerMethodRegexPointcut extends JdkRegexpMethodPointcut {

	private static final long serialVersionUID = 8342662318985403824L;

	public TracerMethodRegexPointcut() {
		super.setPatterns(patterns);
	}

	@Override
	public ClassFilter getClassFilter() {
		final ClassFilter filter = super.getClassFilter();
		return new ClassFilter() {
			public boolean matches(Class clazz) {
				int modifier = clazz.getModifiers();
				if (Modifier.isFinal(modifier)) {
					return false;
				}
				String clazzName = clazz.getName();
				if (beenFilted(clazzName)) {
					return false;
				} else {
					return filter.matches(clazz);
				}
			}
		};
	}

	private final static String[] patterns = new String[] { ".*" };

	private final static String[] FilterClazzPrefix = new String[] { "java.",// <br>
			"javax.", "org.springframework.", "org.hibernate.", "com.ibatis." };

	/**
	 * 过滤package
	 * 
	 * @param clazzName
	 * @return
	 */
	public static boolean beenFilted(String clazzName) {
		for (String prefix : FilterClazzPrefix) {
			if (clazzName.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}
}
