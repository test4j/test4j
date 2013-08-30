package org.jtester.testng.spring.utility;

import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.SpringInitMethod;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("unused")
@SpringContext
@AutoBeanInject
@Test(groups = "jtester")
public class JTesterReflectorTest_FieldProxy extends JTester {
	@SpringBeanByName
	ProxyBean proxyBean;

	@SpringBeanFrom
	AutoBean autoBean = new AutoBean() {
		{
			want.object(proxyBean).isNull();
			this.proxyBean = reflector.newProxy(JTesterReflectorTest_FieldProxy.class, "proxyBean");
		}
	};

	public void test_GetFieldProxy() {
		String result = this.autoBean.call();
		want.string(result).isEqualTo("call");
		ProxyBean proxy = this.autoBean.getProxyBean();
		want.object(proxy).not(the.object().same(this.proxyBean));
	}

	public static class AutoBean {
		ProxyBean proxyBean;

		public String call() {
			return this.proxyBean.call();
		}

		public ProxyBean getProxyBean() {
			return proxyBean;
		}
	}

	public static class ProxyBean {
		public String call() {
			return "call";
		}
	}
}
