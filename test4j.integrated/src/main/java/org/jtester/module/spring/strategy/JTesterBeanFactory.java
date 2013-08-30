package org.jtester.module.spring.strategy;

import javax.sql.DataSource;

import org.jtester.module.database.environment.DBEnvironmentFactory;
import org.jtester.tools.commons.ConfigHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

@SuppressWarnings("rawtypes")
public class JTesterBeanFactory extends DefaultListableBeanFactory {

	// private boolean ignoreNoSuchBean;

	public JTesterBeanFactory(final BeanFactory parentBeanFactory) {// , boolean
																	// ignoreNoSuchBean
		super(parentBeanFactory);
		// this.ignoreNoSuchBean = ignoreNoSuchBean;
	}

	@Override
	public Object getBean(final String name, final Class requiredType, final Object[] args) throws BeansException {
		// try {
		Object bean = getMyBean(name, requiredType, args);
		return bean;
		// } catch (NoSuchBeanDefinitionException e) {
		// if (ignoreNoSuchBean) {
		// JTesterLogger.info("Ignore NoSuchBeanDefinitionException:" +
		// e.getMessage());
		// return null;
		// } else {
		// throw e;
		// }
		// }
	}

	/**
	 * 返回容器中的spring对象
	 * 
	 * @param name
	 * @return
	 */
	public Object getSpringBean(final String name) {
		Object bean = this.getBean(name, null, null);
		return bean;
	}

	/**
	 * 返回依赖项的代理对象（对象的调用转向spring容器中的spring bean）<br>
	 * <br>
	 * {@inheritDoc}
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public Object getBean(final String name) {
		return this.getProxyBean(name);
	}

	/**
	 * 返回依赖项的代理对象（对象的调用转向spring容器中的spring bean）
	 * 
	 * @param name
	 * @return
	 */
	public Object getProxyBean(final String name) {
		return this.getBean(name, null, null);
	}

	private Object getMyBean(final String name, final Class requiredType, final Object[] args) throws BeansException {
		if (ConfigHelper.isSpringDataSourceName(name)) {
			DataSource dataSource = DBEnvironmentFactory.getDefaultDBEnvironment()
					.getDataSourceAndActivateTransactionIfNeeded();
			return dataSource;
		} else {
			Object bean = super.getBean(name, requiredType, args);
			return bean;
		}
	}
}
