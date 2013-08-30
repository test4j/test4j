package org.jtester.module.spring.utility;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext
@AutoBeanInject
public class JTesterReflectorTest_FieldProxy implements JTester {
    @SpringBeanByName
    ProxyBean proxyBean;

    @SpringBeanFrom
    AutoBean  autoBean = new AutoBean() {
                           {
                               want.object(proxyBean).isNull();
                               this.proxyBean = reflector.newProxy(JTesterReflectorTest_FieldProxy.class, "proxyBean");
                           }
                       };

    @Test
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
