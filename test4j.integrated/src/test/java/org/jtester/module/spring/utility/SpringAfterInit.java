package org.jtester.module.spring.utility;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringAfterInit implements BeanFactoryAware {
	private String prop = "uninit";

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.prop = initProp;
	}

	public String getProp() {
		return prop;
	}

	public static String initProp = "unset";
}
