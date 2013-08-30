package org.test4j.module.spring.strategy;

import org.test4j.module.spring.strategy.ImplementorFinder;
import org.test4j.tools.commons.ClazzHelper;

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
