package org.jtester.json.encoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

import org.jtester.json.JSON;
import org.jtester.json.encoder.beans.test.GenicBean;
import org.jtester.json.encoder.beans.test.TestedIntf;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.json.helper.JSONFeature;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Test(groups = { "jtester", "json" })
public class JSONEncoderTest extends JTester {

	@Test(dataProvider = "objects")
	public void testEncode(Object obj, String expected) {
		String json = JSON.toJSON(obj, JSONFeature.UseSingleQuote);
		want.string(json).contains(expected);
	}

	@DataProvider
	public Object[][] objects() {
		return new Object[][] {
				{ User.newInstance(12, "darui.wu"), "#class:'org.jtester.json.encoder.beans.test.User@" },// <br>
				{ new int[] { 1, 2, 3 }, "#class:'int[]@" },// <br>
				{ new HashMap(), "#class:'map@" },// <br>
				{ new ArrayList(), "#class:'list@" } // <br>
		};
	}

	@Test(description = "对象循环引用，并且不输出class的情况")
	public void testRefObject() {
		GenicBean bean = new GenicBean();
		bean.setName("genicBean");
		bean.setRefObject(bean);
		String json = JSON.toJSON(bean, JSONFeature.UnMarkClassFlag, JSONFeature.UseSingleQuote);
		want.string(json).eqIgnoreSpace("{name:'genicBean',refObject:null}");
	}

	@Test(description = "对象循环引用，且输出class的情况")
	public void testRefObject_MarkClazz() {
		GenicBean bean = new GenicBean();
		bean.setName("genicBean");
		bean.setRefObject(bean);
		String json = JSON.toJSON(bean, JSONFeature.UseSingleQuote);
		want.string(json).contains("#class:'org.jtester.json.encoder.beans.test.GenicBean@")
				.contains("refObject:{#refer:@");
	}

	@Test(description = "对象是匿名类")
	public void testEncode_ProxyClazz() {
		User user = new User() {
			{
				this.setId(124);
				this.setName("my name");
			}
		};
		String json = JSON.toJSON(user, JSONFeature.UseSingleQuote);
		System.out.println(json);
		want.string(json).contains("#class:'org.jtester.json.encoder.beans.test.User@");
	}

	public void testFilterClass() {
		Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { TestedIntf.class },
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return null;
					}
				});

		GenicBean bean = new GenicBean();
		bean.setName("filter proxy");
		bean.setRefObject(proxy);
		String json = JSON.toJSON(bean, JSONFeature.UseSingleQuote);
		want.string(json).contains("refObject:null");
	}
}
