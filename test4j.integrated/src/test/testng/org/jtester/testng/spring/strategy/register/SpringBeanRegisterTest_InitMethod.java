package org.jtester.testng.spring.strategy.register;

import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanByType;
import org.jtester.module.spring.annotations.AutoBeanInject.BeanMap;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
		@BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
@Test(groups = "jtester")
public class SpringBeanRegisterTest_InitMethod extends JTester {
	@SpringBeanByName(claz = InitMethodBeanByName.class, init = "init")
	InitMethodBeanByName beanByName;

	@SpringBeanByType(value = InitMethodBeanByType.class, init = "init")
	InitMethodBeanByType beanByType;

	@Test(description = "自动注册bean时识别init-method")
	public void testBeanDefinitionByName_InitMethod() {
		String result = beanByName.say();
		want.string(result).isEqualTo("bean init");
	}

	@Test(description = "自动注册bean时识别init-method")
	public void testBeanDefinitionByType_InitMethod() {
		String result = beanByType.say();
		want.string(result).isEqualTo("bean init");
	}

	public static class InitMethodBeanByName {

		private String name = "uninit";

		public String say() {
			return name;
		}

		public void init() {
			this.name = "bean init";
		}
	}

	public static class InitMethodBeanByType {

		private String name = "uninit";

		public String say() {
			return name;
		}

		public void init() {
			this.name = "bean init";
		}
	}
}
