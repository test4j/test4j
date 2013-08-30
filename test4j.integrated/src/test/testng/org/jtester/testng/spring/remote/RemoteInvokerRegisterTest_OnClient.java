package org.jtester.testng.spring.remote;

import mockit.Mock;

import org.jtester.module.spring.annotations.SpringBeanRemote.SpringBeanRemoteType;
import org.jtester.module.spring.remote.RemoteInvokerRegister;
import org.jtester.testng.JTester;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.testng.annotations.Test;

@SuppressWarnings({  "rawtypes" })
@Test(groups = "jtester")
public class RemoteInvokerRegisterTest_OnClient extends JTester {
	@Test
	public void testRegisterSpringBeanRemoteOnClient() {
		new MockUp<RemoteInvokerRegister>() {
			@Mock
			public RootBeanDefinition newClientRemoteInvokerBeanDefinition(final String beanID,
					final String serviceUrl, final Class serviceInterface, SpringBeanRemoteType type) {
				want.string(beanID).isEqualTo("hessian/bean");
				want.string(serviceUrl).isEqualTo("${springbean.remote.baseurl}/hessian/bean");
				want.object(type).isEqualTo(SpringBeanRemoteType.hessian);
				return new RootBeanDefinition();
			}
		};
		new MockUp<AbstractApplicationContext>() {
			@Mock
			public Object getBean(String name) throws BeansException {
				want.string(name).isEqualTo("hessian/bean");
				return null;
			}
		};
		RemoteInvokerRegister.registerSpringBeanRemoteOnClient(new DefaultListableBeanFactory(),
				RemoteInvokerRegisterTest_OnClientHelper.DefaultRegister.class);

		RemoteInvokerRegister.injectSpringBeanRemote(new StaticWebApplicationContext(),
				new RemoteInvokerRegisterTest_OnClientHelper.DefaultRegister());
	}

	@Test
	public void testRegisterSpringBeanRemoteOnClient_HessianUrl() {
		new MockUp<RemoteInvokerRegister>() {
			@Mock
			public RootBeanDefinition newClientRemoteInvokerBeanDefinition(final String beanID,
					final String serviceUrl, final Class serviceInterface, SpringBeanRemoteType type) {
				want.string(beanID).isEqualTo("hessian/bean");
				want.string(serviceUrl).isEqualTo("${springbean.remote.baseurl}/hessian/bean");
				want.object(type).isEqualTo(SpringBeanRemoteType.hessian);
				return new RootBeanDefinition();
			}
		};
		new MockUp<AbstractApplicationContext>() {
			@Mock
			public Object getBean(String name) throws BeansException {
				want.string(name).isEqualTo("hessian/bean");
				return null;
			}
		};
		RemoteInvokerRegister.registerSpringBeanRemoteOnClient(new DefaultListableBeanFactory(),
				RemoteInvokerRegisterTest_OnClientHelper.RegisterHessian.class);
		RemoteInvokerRegister.injectSpringBeanRemote(new StaticWebApplicationContext(),
				new RemoteInvokerRegisterTest_OnClientHelper.RegisterHessian());
	}

	@Test
	public void testRegisterSpringBeanRemoteOnClient_RegisterHttpInvokerURL() {
		new MockUp<RemoteInvokerRegister>() {
			@Mock
			public RootBeanDefinition newClientRemoteInvokerBeanDefinition(final String beanID,
					final String serviceUrl, final Class serviceInterface, SpringBeanRemoteType type) {
				want.string(beanID).isEqualTo("httpinvoker/bean");
				want.string(serviceUrl).isEqualTo("${springbean.remote.baseurl}/httpinvoker/bean");
				want.object(type).isEqualTo(SpringBeanRemoteType.httpinvoker);
				return new RootBeanDefinition();
			}
		};
		new MockUp<AbstractApplicationContext>() {
			@Mock
			public Object getBean(String name) throws BeansException {
				want.string(name).isEqualTo("httpinvoker/bean");
				return null;
			}
		};

		RemoteInvokerRegister.registerSpringBeanRemoteOnClient(new DefaultListableBeanFactory(),
				RemoteInvokerRegisterTest_OnClientHelper.RegisterHttpInvoker.class);

		RemoteInvokerRegister.injectSpringBeanRemote(new StaticWebApplicationContext(),
				new RemoteInvokerRegisterTest_OnClientHelper.RegisterHttpInvoker());
	}

	@Test
	public void testRegisterSpringBeanRemoteOnClient_RegisterBoth() {
		final boolean[] newBean_visited = new boolean[] { false, false };
		final boolean[] getBean_Visited = new boolean[] { false, false };
		new MockUp<RemoteInvokerRegister>() {

			@Mock
			public RootBeanDefinition newClientRemoteInvokerBeanDefinition(final String beanID,
					final String serviceUrl, final Class serviceInterface, SpringBeanRemoteType type) {

				if (beanID.equals("httpinvoker/bean")) {
					newBean_visited[0] = true;
					want.string(serviceUrl).isEqualTo("${springbean.remote.baseurl}/httpinvoker/bean");
					want.object(type).isEqualTo(SpringBeanRemoteType.httpinvoker);
				} else if (beanID.equals("hessian/bean")) {
					newBean_visited[1] = true;
					want.string(serviceUrl).isEqualTo("${springbean.remote.baseurl}/hessian/bean");
					want.object(type).isEqualTo(SpringBeanRemoteType.hessian);
				}
				return new RootBeanDefinition();
			}
		};

		new MockUp<AbstractApplicationContext>() {
			@Mock
			public Object getBean(String name) throws BeansException {
				if (name.equals("httpinvoker/bean")) {
					getBean_Visited[0] = true;
				} else if (name.equals("hessian/bean")) {
					getBean_Visited[1] = true;
				}
				return null;
			}
		};
		RemoteInvokerRegister.registerSpringBeanRemoteOnClient(new DefaultListableBeanFactory(),
				RemoteInvokerRegisterTest_OnClientHelper.RegisterBoth.class);

		RemoteInvokerRegister.injectSpringBeanRemote(new StaticWebApplicationContext(),
				new RemoteInvokerRegisterTest_OnClientHelper.RegisterBoth());

		want.array(newBean_visited).isEqualTo(new Boolean[] { true, true });
		want.array(getBean_Visited).isEqualTo(new Boolean[] { true, true });
	}
}
