package org.jtester.testng.spring.utility;

import org.jtester.module.database.IDatabase;
import org.jtester.module.spring.ISpring;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.utility.SpringAfterInit;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester" })
@SpringContext("org/jtester/module/spring/testedbeans/xml/data-source.cglib.xml")
@AutoBeanInject
public class SpringTestHelperTest extends JTester implements IDatabase, ISpring {

	@SpringBeanByName(claz = SpringAfterInit.class)
	SpringAfterInit bean;

	@Test
	public void testInvalidate_SpringUnInvalidate() {
		String ret = bean.getProp();
		want.string(ret).isEqualTo("unset");

		SpringAfterInit.initProp = "invalid";
		spring.invalidate();
	}

	@Test(dependsOnMethods = "testInvalidate_SpringUnInvalidate")
	public void testInvalidate_SpringINvalidate() {
		String ret = bean.getProp();
		want.string(ret).isEqualTo("invalid");
	}
}
