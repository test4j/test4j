package org.test4j.testng.spring.utility;

import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext
@AutoBeanInject
@Test(groups = "test4j")
public class Test4JReflectorTest_FieldProxy extends Test4J {
    @SpringBeanByName
    ProxyBean proxyBean;

    @SpringBeanFrom
    AutoBean  autoBean = new AutoBean() {
                           {
                               want.object(proxyBean).isNull();
                               this.proxyBean = reflector.newProxy(Test4JReflectorTest_FieldProxy.class, "proxyBean");
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
