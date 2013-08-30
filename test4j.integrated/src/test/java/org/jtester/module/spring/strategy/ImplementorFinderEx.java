package org.jtester.module.spring.strategy;

import org.jtester.tools.commons.ClazzHelper;

public class ImplementorFinderEx extends ImplementorFinder {

	public static String replace(String interfaceKey, String implementKey, String interfaceClass) {
		return ImplementorFinder.replace(interfaceKey, implementKey, interfaceClass);
	}

	public static String[] splitIntfClazzByExpression(String intfClazz, String[] exps) {
		return ImplementorFinder.splitIntfClazzByExpression(intfClazz, exps);
	}

	public static boolean regMatch(String interfaceKey, String interfaceClass) {
		String regex = ClazzHelper.getPackageRegex(interfaceKey);
		return interfaceClass.matches(regex);
	}
}
