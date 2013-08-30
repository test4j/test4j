package org.jtester.module.spring.strategy.register;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.module.spring.exception.FindBeanImplClassException;
import org.jtester.module.spring.strategy.ImplementorFinder;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.StringHelper;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * spring bean定义
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
public class RegisterBeanDefinition {

	private final DefaultListableBeanFactory factory;

	private final AutoBeanInject autoBeanInject;

	private final List<BeanMap> mappingRules;

	private boolean allowAutoInject;

	public RegisterBeanDefinition(final DefaultListableBeanFactory factory, final AutoBeanInject autoBeanInject) {
		this.factory = factory;
		this.autoBeanInject = autoBeanInject;
		if (autoBeanInject == null || autoBeanInject.value() == false) {
			this.allowAutoInject = false;
			this.mappingRules = new ArrayList<BeanMap>();
		} else {
			this.allowAutoInject = true;
			this.mappingRules = checkBeanMap(this.autoBeanInject.maps());
		}
	}

	private final static String VALID_PACK_REGEX = "[\\w_$\\.\\*]+";

	/**
	 * 验证@BeanMap属性的有效性
	 * 
	 * @param autoMaps
	 * @return
	 */
	private static List<BeanMap> checkBeanMap(BeanMap[] autoMaps) {
		List<BeanMap> mapping = new ArrayList<BeanMap>();
		if (autoMaps == null || autoMaps.length == 0) {
			return mapping;
		}
		for (BeanMap autoMap : autoMaps) {
			String intf = autoMap.intf();
			String impl = autoMap.impl();
			if (StringHelper.isBlankOrNull(intf) || StringHelper.isBlankOrNull(impl)) {
				throw new RuntimeException(String.format("Illegal value @AutoMap(intf=\"%s\",impl=\"%s\") value", intf,
						impl));
			}
			if (intf.matches(VALID_PACK_REGEX) == false || impl.matches(VALID_PACK_REGEX) == false) {
				throw new RuntimeException(String.format("Illegal value @AutoMap(intf=\"%s\",impl=\"%s\") value", intf,
						impl));
			}
			mapping.add(autoMap);
		}
		return mapping;
	}

	/**
	 * 改id的bean是否已经定义
	 * 
	 * @param beanName
	 * @return
	 */
	public boolean doesHaveRegisted(String beanName) {
		boolean doesRegisted = factory.containsBeanDefinition(beanName);
		return doesRegisted;
	}

	public Class findImplementClass(Class ownerClazz, String propName, Class propClazz)
			throws FindBeanImplClassException {
		Class implClazz = ImplementorFinder.findImplClazz(ownerClazz, propName, propClazz, mappingRules);
		return implClazz;
	}

	public void register(String beanName, AbstractBeanDefinition beanDefinition) {
		factory.registerBeanDefinition(beanName, beanDefinition);
	}

	/**
	 * 如果忽略异常，则只打印消息；否则抛出运行时异常
	 * 
	 * @param e
	 */
	public void ignoreNotFoundException(Throwable e) {
		if (this.autoBeanInject == null || this.autoBeanInject.ignoreNotFound()) {
			MessageHelper.warn("ignore NotFound:" + (e == null ? "<null>" : e.getMessage()));
		} else {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断是否允许自动注入接口bean(自动查找实现)
	 * 
	 * @return
	 */
	public boolean allowAutoInject() {
		return this.allowAutoInject;
	}

	/**
	 * 判断是否运行注册该属性<br>
	 * 
	 * @param beanName
	 * @return
	 */
	public boolean allowAutoInject(String beanName) {
		if (this.allowAutoInject == false) {
			return false;
		}
		boolean registed = this.doesHaveRegisted(beanName);
		return !registed;
	}

	/**
	 * 默认会被排除掉的属性名称
	 */
	private static Set<String> DEFAULT_EXCLUDE_PROPERTIES = new HashSet<String>() {
		private static final long serialVersionUID = 705657590280935844L;
		{
			add("class");
			add("override");
		}
	};

	/**
	 * 默认会被排除掉自动注册的属性包类型
	 */
	private static Set<String> DEFAULT_EXCLUDE_PACKAGES = new HashSet<String>() {
		private static final long serialVersionUID = -102059987060939894L;
		{
			add("org.springframework.");
			add("java.");
			add("javax.");
			add("org.spring.");
		}
	};

	/**
	 * 是否排除该属性的注册<br>
	 * o 枚举类型、Annotation，数组或者primitive类型<br>
	 * o 默认排除的属性，如 Object 定义的class,ovveride<br>
	 * o 显式排除的类型，如java.*, javax.*和org.spring.*等<br>
	 * o 已经在factory中定义过的属性<br>
	 * 
	 * @param name
	 * @param clazz
	 * @return
	 */
	public boolean isExcludeProperty(String name, Class clazz) {
		if (clazz.isEnum() || clazz.isAnnotation() || clazz.isArray() || clazz.isPrimitive()) {
			return true;
		}

		boolean isExcludeProperty = isExcludeProperty(name);
		if (isExcludeProperty) {
			return true;
		}
		boolean isExcludeClazz = isExcludePackage(clazz.getName());
		return isExcludeClazz;
	}

	/**
	 * 是否排除注入的指定的属性
	 * 
	 * @param property
	 *            要注入bean的属性
	 * @param beanFactory
	 *            spring容器
	 * @param excludeProperties
	 *            显式指定要排除的属性名称
	 * @return
	 */
	private boolean isExcludeProperty(String property) {
		if (StringHelper.isBlankOrNull(property)) {
			return true;
		}
		if (DEFAULT_EXCLUDE_PROPERTIES.contains(property) || factory.containsBeanDefinition(property)) {
			return true;
		}
		String[] excludeProperties = this.autoBeanInject.excludeProperties();
		if (excludeProperties == null || excludeProperties.length == 0) {
			return false;
		}
		for (String excludeProperty : excludeProperties) {
			if (property.equals(excludeProperty)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 以下属性不能自动注入<br>
	 * o spring本身提供的bean必须在文件中配置 <br>
	 * o java.* 和 javax.* package<br>
	 * o @AutoBeanInject 中显式排除的package
	 * 
	 * @param clazzname
	 *            要注入的属性类型
	 * @param excludePackages
	 *            显式排除的类型
	 * @return
	 */
	private boolean isExcludePackage(String clazzname) {
		for (String excludePackage : DEFAULT_EXCLUDE_PACKAGES) {
			if (clazzname.startsWith(excludePackage)) {
				return true;
			}
		}

		final String[] excludePackages = this.autoBeanInject.excludePackages();
		if (excludePackages == null || excludePackages.length == 0) {
			return false;
		}
		for (String excludePackage : excludePackages) {
			String regex = ClazzHelper.getPackageRegex(excludePackage);
			if (clazzname.matches(regex)) {
				return true;
			}
		}
		return false;
	}
}
