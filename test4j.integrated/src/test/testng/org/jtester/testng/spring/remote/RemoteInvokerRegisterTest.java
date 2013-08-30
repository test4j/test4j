package org.jtester.testng.spring.remote;

import java.util.Properties;

import mockit.Mock;

import org.jtester.module.spring.remote.RemoteInvokerRegister;
import org.jtester.testng.JTester;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class RemoteInvokerRegisterTest extends JTester {
	int count = 0;

	/**
	 * 测试jtester-remote.properties中的注册的remote type没有指定的case
	 */
	@Test
	public void testRegisterSpringBeanRemoteOnServer_Default() {
		count = 0;
		Properties properties = new Properties() {
			private static final long serialVersionUID = -2097717481700457987L;

			{
				this.setProperty("esbChannelTaskManager", "org.esb.martini.EsbChannelTaskManager");
			}
		};
		new MockUp<RemoteInvokerRegister>() {
			@Mock
			public RootBeanDefinition newServerRemoteInvokerBeanDefinition(final String beanID,
					final String serviceInterface, final String invokerExporterClazz) {
				want.string(beanID).isEqualTo("esbChannelTaskManager");
				want.string(serviceInterface).isEqualTo("org.esb.martini.EsbChannelTaskManager");
				want.string(invokerExporterClazz).isEqualTo(HessianServiceExporter.class.getName());
				count++;
				return new RootBeanDefinition();
			}
		};
		new MockUp<DefaultListableBeanFactory>() {
			@Mock
			public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
				want.string(beanName).isEqualTo("/hessian/esbChannelTaskManager");
			}
		};

		RemoteInvokerRegister.registerSpringBeanRemoteOnServer(properties, new DefaultListableBeanFactory());
		want.number(count).isEqualTo(1);
	}

	/**
	 * 测试jtester-remote.properties中的注册的remote type显式指定为httpInvoker的情况
	 */
	@Test
	public void testRegisterSpringBeanRemoteOnServer_HttpInvoker() {
		count = 0;
		Properties properties = new Properties() {
			private static final long serialVersionUID = -2097717481700457987L;

			{
				this.setProperty("esbChannelTaskManager", "org.esb.martini.EsbChannelTaskManager<httpinvoker>");
			}
		};
		new MockUp<RemoteInvokerRegister>() {
			@Mock
			public RootBeanDefinition newServerRemoteInvokerBeanDefinition(final String beanID,
					final String serviceInterface, final String invokerExporterClazz) {
				want.string(beanID).isEqualTo("esbChannelTaskManager");
				want.string(serviceInterface).isEqualTo("org.esb.martini.EsbChannelTaskManager");
				want.string(invokerExporterClazz).isEqualTo(HttpInvokerServiceExporter.class.getName());
				count++;
				return new RootBeanDefinition();
			}
		};
		new MockUp<DefaultListableBeanFactory>() {
			@Mock
			public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
				want.string(beanName).isEqualTo("/httpinvoker/esbChannelTaskManager");
			}
		};

		RemoteInvokerRegister.registerSpringBeanRemoteOnServer(properties, new DefaultListableBeanFactory());
		want.number(count).isEqualTo(1);
	}

	/**
	 * 测试jtester-remote.properties中的注册的remote type显式指定为httpInvoker和hessian的2种情况
	 */
	@Test
	public void testRegisterSpringBeanRemoteOnServer_BothHttpInvokerAndHessian() {
		count = 0;
		Properties properties = new Properties() {
			private static final long serialVersionUID = -2097717481700457987L;

			{
				this.setProperty("esbChannelTaskManager",
						"org.esb.martini.EsbChannelTaskManager<httpinvoker | hessian>");
			}
		};
		new MockUp<RemoteInvokerRegister>() {
			@Mock
			public RootBeanDefinition newServerRemoteInvokerBeanDefinition(final String beanID,
					final String serviceInterface, final String invokerExporterClazz) {
				count++;
				want.string(beanID).isEqualTo("esbChannelTaskManager");
				want.string(serviceInterface).isEqualTo("org.esb.martini.EsbChannelTaskManager");
				if (count == 1) {
					want.string(invokerExporterClazz).isEqualTo(HttpInvokerServiceExporter.class.getName());
				} else {
					want.string(invokerExporterClazz).isEqualTo(HessianServiceExporter.class.getName());
				}
				return new RootBeanDefinition();
			}
		};

		RemoteInvokerRegister.registerSpringBeanRemoteOnServer(properties, new DefaultListableBeanFactory());
		want.number(count).isEqualTo(2);
	}

	/**
	 * 非法的属性格式
	 */
	@Test(expectedExceptions = RuntimeException.class)
	public void testRegisterSpringBeanRemoteOnServer_illegalValueFormat() {
		Properties properties = new Properties() {
			private static final long serialVersionUID = -2097717481700457987L;

			{
				this.setProperty("esbChannelTaskManager", "org.esb.martini.EsbChannelTaskManager<httpinvoker | hessian");
			}
		};

		RemoteInvokerRegister.registerSpringBeanRemoteOnServer(properties, new DefaultListableBeanFactory());
	}
}
