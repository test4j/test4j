package org.jtester.module.spring.remote;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.jtester.module.JTesterException;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.spring.annotations.SpringBeanRemote;
import org.jtester.module.spring.annotations.SpringBeanRemote.SpringBeanRemoteType;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.FieldHelper;
import org.jtester.tools.commons.StringHelper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * Spring httpInvoker动态注册器<br>
 * o hessian <br>
 * o httpinvoker<br>
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RemoteInvokerRegister {

	private static final String SPRINGBEAN_REMOTE_BASEURL = "${springbean.remote.baseurl}";

	/**
	 * 动态注册 @RemoteInvokerBean 注册的bean
	 * 
	 * @param beanFactory
	 * @param testedClazz
	 */
	public static void registerSpringBeanRemoteOnClient(final DefaultListableBeanFactory beanFactory,
			final Class testedClazz) {
		Set<Field> fields_HttpInvoker = AnnotationHelper.getFieldsAnnotatedWith(testedClazz, SpringBeanRemote.class);
		for (Field field : fields_HttpInvoker) {
			SpringBeanRemote springBeanRemote = field.getAnnotation(SpringBeanRemote.class);
			String beanID = springBeanRemote.value();
			if (StringHelper.isBlankOrNull(beanID)) {
				beanID = field.getName();
			}
			SpringBeanRemoteType type = SpringBeanRemoteType.getTypeFromURL(beanID);
			if (type == null) {
				type = springBeanRemote.type();
				beanID = type.name() + "/" + beanID;
			}

			if (beanFactory.containsBeanDefinition(beanID)) {
				MessageHelper.info(String.format("spring bean[%s] has been defined in application context!", beanID));
				return;
			}
			String serviceUrl = springBeanRemote.serviceUrl();

			if (StringHelper.isBlankOrNull(serviceUrl)) {
				serviceUrl = SPRINGBEAN_REMOTE_BASEURL + "/" + beanID;
			}
			Class serviceInterface = springBeanRemote.serviceInterface();
			if (serviceInterface == null || serviceInterface == Object.class) {
				serviceInterface = field.getType();
			}

			RootBeanDefinition beanDefinition = newClientRemoteInvokerBeanDefinition(beanID, serviceUrl,
					serviceInterface, type);
			beanFactory.registerBeanDefinition(beanID, beanDefinition);
		}
	}

	/**
	 * 往测试Fixture中注入httpInvoker/hessian bean
	 * 
	 * @param ctx
	 * @param testedObject
	 */
	public static void injectSpringBeanRemote(final ApplicationContext ctx, Object testedObject) {
		Set<Field> fields_HttpInvoker = AnnotationHelper.getFieldsAnnotatedWith(testedObject.getClass(),
				SpringBeanRemote.class);
		for (Field field : fields_HttpInvoker) {
			SpringBeanRemote springBeanRemote = field.getAnnotation(SpringBeanRemote.class);
			String beanID = springBeanRemote.value();
			if (StringHelper.isBlankOrNull(beanID)) {
				beanID = field.getName();
			}
			SpringBeanRemoteType type = SpringBeanRemoteType.getTypeFromURL(beanID);
			if (type == null) {
				type = springBeanRemote.type();
				beanID = type.name() + "/" + beanID;
			}

			try {
				Object bean = ctx.getBean(beanID);
				FieldHelper.setFieldValue(testedObject, field, bean);
			} catch (Throwable e) {
				throw new JTesterException(
						"Unable to assign the Spring http invoker bean to field annotated with @HttpInvoker", e);
			}
		}
	}

	/**
	 * 定义httpInvoker客户端调用bean
	 * 
	 * @param beanID
	 * @param serviceUrl
	 * @param serviceInterface
	 * @return
	 */
	private static RootBeanDefinition newClientRemoteInvokerBeanDefinition(final String beanID,
			final String serviceUrl, final Class serviceInterface, SpringBeanRemoteType type) {
		if (serviceInterface.isInterface() == false) {
			String error = String.format("httpInvoker[%s] service interface must be an interface, but actual is:%s",
					beanID, serviceInterface.getName());
			throw new RuntimeException(error);
		}

		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		if (type == SpringBeanRemoteType.httpinvoker) {
			beanDefinition.setBeanClassName("org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean");
		} else if (type == SpringBeanRemoteType.hessian) {
			beanDefinition.setBeanClassName("org.springframework.remoting.caucho.HessianProxyFactoryBean");
		} else {
			throw new RuntimeException("unsupport remote inovker bype:" + type);
		}
		beanDefinition.setScope("singleton");
		beanDefinition.setAutowireCandidate(true);
		MutablePropertyValues properties = new MutablePropertyValues(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

			{
				this.put("serviceUrl", serviceUrl);
				this.put("serviceInterface", serviceInterface.getName());
			}
		});
		beanDefinition.setPropertyValues(properties);

		beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);

		return beanDefinition;
	}

	/**
	 * 注册服务器端的httpInvoker bean
	 * 
	 * @param beanFactory
	 */
	public static void registerSpringBeanRemoteOnServer(final Properties properties,
			final DefaultListableBeanFactory beanFactory) {
		Enumeration<?> en = properties.keys();
		while (en.hasMoreElements()) {
			String bean = (String) en.nextElement();
			String claz = properties.getProperty(bean);
			int start = claz.indexOf("<");
			String[] types = new String[] { "hessian" };
			if (start > 0 && claz.endsWith(">")) {
				types = claz.substring(start + 1, claz.length() - 1).split("\\|");
				claz = claz.substring(0, start);
			}

			if (types == null || types.length == 0) {
				types = new String[] { "hessian" };
			}
			for (String type : types) {
				if (type == null) {
					continue;
				}
				type = type.trim();

				if (type.equalsIgnoreCase("httpinvoker")) {
					RootBeanDefinition beanDefinition = newServerRemoteInvokerBeanDefinition(bean, claz,
							HttpInvokerServiceExporter.class.getName());
					beanFactory.registerBeanDefinition("/httpinvoker/" + bean, beanDefinition);
					MessageHelper.info("register httpinvoker bean[/httpinvoker/" + bean + "] successfully.");
				} else if (type.equalsIgnoreCase("hessian")) {
					RootBeanDefinition beanDefinition = newServerRemoteInvokerBeanDefinition(bean, claz,
							HessianServiceExporter.class.getName());
					beanFactory.registerBeanDefinition("/hessian/" + bean, beanDefinition);
					MessageHelper.info("register httpinvoker bean[/hessian/" + bean + "] successfully.");
				} else {
					throw new RuntimeException(String.format(
							"unsupport remote invoker type[%s], only support hessian or httpinvoker.", type));
				}
			}
		}
	}

	private static RootBeanDefinition newServerRemoteInvokerBeanDefinition(final String beanID,
			final String serviceInterface, final String invokerExporterClazz) {
		if (StringHelper.isBlankOrNull(beanID) || StringHelper.isBlankOrNull(serviceInterface)) {
			throw new RuntimeException("illegal properties file[classpath:jtester-remote.properties].");
		}

		try {
			Class claz = Class.forName(serviceInterface);
			if (claz.isInterface() == false) {
				String error = String.format(
						"httpInvoker[%s] service interface must be an interface, but actual is:%s", beanID,
						serviceInterface);
				throw new RuntimeException(error);
			}
		} catch (ClassNotFoundException e) {
			String error = String.format("define bean[%s] error.", invokerExporterClazz);
			throw new RuntimeException(error, e);
		}

		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClassName(invokerExporterClazz);
		beanDefinition.setScope("singleton");
		beanDefinition.setAutowireCandidate(true);
		MutablePropertyValues properties = new MutablePropertyValues(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
				this.put("service", new RuntimeBeanReference(beanID));
				this.put("serviceInterface", serviceInterface);
			}
		});
		beanDefinition.setPropertyValues(properties);

		beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);

		return beanDefinition;
	}
}
