package org.jtester.module.spring.strategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.spring.annotations.SpringInitMethod;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.exception.FindBeanImplClassException;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.ArrayHelper;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.StringHelper;

/**
 * 根据表达式查找接口实现的工具类
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class ImplementorFinder {

	/**
	 * 根据表达式寻找接口的实现<br>
	 * 如果是beanClazz本身就是可实例类，直接返回beanClazz
	 * 
	 * @param ownerClazz
	 *            拥有springbean字段的类(方便输出异常消息，明确是那个类查找依赖出现了错误)
	 * @param beanName
	 *            spring bean名称
	 * @param beanClazz
	 *            spring bean的定义类型
	 * @param beanMapping
	 *            查找spring bean实现类的规则集
	 * @return spring bean的实现类
	 * @throws FindBeanImplClassException
	 */
	@SuppressWarnings("unchecked")
	public static Class findImplClazz(final Class ownerClazz, final String beanName, final Class beanClazz,
			final List<BeanMap> beanMapping) throws FindBeanImplClassException {
		Class implClazz = beanClazz;
		if (ClazzHelper.isInterfaceOrAbstract(beanClazz)) {
			try {
				implClazz = ImplementorFinder.getImplClass(beanClazz, beanMapping);
			} catch (FindBeanImplClassException e) {
				String message = String.format("Look for spring bean for property[%s] of class[%s] error!", beanName,
						ownerClazz.getName());
				MessageHelper.warn(message);
				throw new FindBeanImplClassException(message, e);
			}
		}

		if (implClazz == null || ClazzHelper.isInterfaceOrAbstract(implClazz)) {
			return null;
		}
		try {
			Constructor c = implClazz.getDeclaredConstructor(new Class[] {});
			if (c == null) {
				return null;
			}
			return implClazz;
		} catch (Throwable e) {
			String error = String.format("find default constructor function of class[%s] error.", implClazz.getName());
			throw new FindBeanImplClassException(error, e);
		}
	}

	/**
	 * 查找spring bean实现的initMethod方法名称
	 * 
	 * @param claz
	 * @return
	 */
	public static String findInitMethodName(final Class claz) {
		Set<Method> methods = AnnotationHelper.getMethodsAnnotatedWith(claz, SpringInitMethod.class);
		if (methods == null || methods.size() == 0) {
			return null;
		}
		Method method = methods.iterator().next();
		return method.getName();
	}

	/**
	 * 返回接口对应的实现类
	 * 
	 * @return 接口对应的实现类
	 * @throws FindBeanImplClassException
	 */
	protected static Class getImplClass(final Class beanClazz, final List<BeanMap> beanMapping)
			throws FindBeanImplClassException {
		final String beanClazzName = beanClazz.getName();
		Class beanImplClazz = null;
		List<String> exceptions = new ArrayList<String>();
		for (BeanMap beanMap : beanMapping) {
			String intf = beanMap.intf();
			String impl = beanMap.impl();
			String beanImplClazzName = null;
			try {
				if (intf.equals(beanClazzName)) {
					beanImplClazz = Class.forName(impl);
				}
				if (intf.contains("*") == false) {// 非regex形式的package无需检查
					continue;
				}
				String regex = ClazzHelper.getPackageRegex(intf);
				if (beanClazzName.matches(regex) == false) {// 不符regex描述的package的无需检查
					continue;
				}
				beanImplClazzName = replace(intf, impl, beanClazzName);

				if (!StringHelper.equals(beanImplClazzName, beanClazzName)) {
					beanImplClazz = ImplementorFinder.class.getClassLoader().loadClass(beanImplClazzName);
				}
			} catch (ClassNotFoundException e) {
				String message = String.format(
						"\nCan't find implement class[%s] of interface[%s], use @BeanMap(intf=\"%s\",impl=\"%s\")",
						beanImplClazzName, beanClazzName, intf, impl);
				exceptions.add(message);
			} catch (NoClassDefFoundError err) {
				String message = String.format(
						"\nCan't find implement class[%s] of interface[%s], use @BeanMap(intf=\"%s\",impl=\"%s\")",
						beanImplClazzName, beanClazzName, intf, impl);
				exceptions.add(message);
			}
		}
		if (beanImplClazz == null) {
			if (exceptions.size() == 0) {
				throw new FindBeanImplClassException("can't find rule to find implement class for " + beanClazzName);
			} else {
				throw new FindBeanImplClassException(ArrayHelper.toString(exceptions.toArray(new String[0])));
			}
		} else {
			return beanImplClazz;
		}
	}

	/**
	 * 根据接口和实现的表达式替换*和** <br>
	 * 
	 * @param interfaceKey
	 * @param implementKey
	 * @param interfaceClass
	 * @return
	 */
	protected static String replace(String interfaceKey, String implementKey, String interfaceClass) {
		String[] intfSplits = splitBy(interfaceKey, "\\*\\*");
		String[] implSplits = splitBy(implementKey, "\\*\\*");

		if (intfSplits.length != implSplits.length) {
			throw new RuntimeException(String.format(
					"interface class expression[%s] and implement class expression[%s] aren't matched!", interfaceKey,
					implementKey));
		}
		String[] intfPacks = splitIntfClazzByExpression(interfaceClass, intfSplits);
		int length = intfSplits.length;
		StringBuffer implementClass = new StringBuffer();

		for (int index = 0; index < length; index++) {
			String intfSplit = intfSplits[index];
			String implSplit = implSplits[index];

			String realPack = replaceAsterisk(intfSplit, implSplit, intfPacks[index * 2]);
			implementClass.append(realPack);

			if (index < length - 1) {// 替换**
				String doubleAsterisk = intfPacks[index * 2 + 1];
				implementClass.append(doubleAsterisk);
			}
		}

		return implementClass.toString();
	}

	/**
	 * 将class的全称按照表达式分割
	 * 
	 * @param intfClazz
	 * @param exps
	 * @return
	 */
	protected static String[] splitIntfClazzByExpression(String intfClazz, String[] exps) {
		StringBuffer regex = new StringBuffer();
		boolean firstExp = true;
		for (String exp : exps) {
			if (firstExp == false) {
				regex.append("(.*)");
			} else {
				firstExp = false;
			}
			regex.append("(" + exp.replace(".", "\\.").replace("*", ClazzHelper.VALID_PACK_REGULAR) + ")");
		}
		Pattern p = Pattern.compile(regex.toString());
		Matcher m = p.matcher(intfClazz);
		if (m.matches() == false) {
			throw new RuntimeException(String.format(
					"interface class expression[%s] and implement class expression[%s] aren't matched!", intfClazz,
					ArrayHelper.toString(exps)));
		}
		int count = m.groupCount();
		if (count != exps.length * 2 - 1) {
			throw new RuntimeException(String.format(
					"can't find enough parts of interface class expression[%s] by expression[%s]!", intfClazz,
					ArrayHelper.toString(exps)));
		}
		String[] packs = new String[count];
		for (int index = 0; index < count; index++) {
			packs[index] = m.group(index + 1);
		}
		return packs;
	}

	/**
	 * 替换*字符
	 * 
	 * @param intf
	 * @param impl
	 * @param intfPack
	 * @return
	 */
	protected static String replaceAsterisk(String intf, String impl, String intfPack) {
		String[] intfItems = splitBy(intf, "\\*");
		String[] implItems = splitBy(impl, "\\*");
		if (intfItems.length != implItems.length) {
			throw new RuntimeException(String.format("item [%s] not match [%s]!", intf, impl));
		}
		String[] words = splitIntfClazzByExpression(intfPack, intfItems);
		int length = intfItems.length;
		StringBuffer implPack = new StringBuffer();

		for (int index = 0; index < length; index++) {
			implPack.append(implItems[index]);
			if (index < length - 1) {// 替换*
				String asterisk = words[index * 2 + 1];
				implPack.append(asterisk);
			}
		}
		return implPack.toString();
	}

	private static String[] splitBy(String input, String regex) {
		String _temp = " " + input + " ";
		String[] splits = _temp.split(regex);
		for (int index = 0; index < splits.length; index++) {
			splits[index] = splits[index].trim();
		}
		return splits;
	}
}
