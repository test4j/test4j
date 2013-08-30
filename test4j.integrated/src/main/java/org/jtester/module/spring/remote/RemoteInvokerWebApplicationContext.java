package org.jtester.module.spring.remote;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class RemoteInvokerWebApplicationContext extends XmlWebApplicationContext {

	@Override
	protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) super.obtainFreshBeanFactory();

		// 读取HttpInvoker配置文件，设置HttpInvoker服务
		try {
			Resource resource = this.getResource(DEFAULT_CONFIG_LOCATION_PREFIX + "jtester-remote.properties");
			InputStream inStream = resource.getInputStream();
			Properties properties = new Properties();
			properties.load(inStream);
			RemoteInvokerRegister.registerSpringBeanRemoteOnServer(properties, beanFactory);

			return beanFactory;
		} catch (Throwable e) {
			throw new RuntimeException("danymic register http invoker bean error.", e);
		}
	}
}
