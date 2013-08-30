package org.jtester.tools.commons;

import org.jtester.testng.JTester;
import org.jtester.tools.commons.ClazzHelper;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class ClazzHelperTest_NewInstance extends JTester {
	@Test
	public void testNewInstance_Abstract() {
		try {
			ClazzHelper.newInstance(AbstractClazz.class);
			want.fail();
		} catch (Exception e) {
			String message = e.getMessage();
			want.string(message).contains("unsupport");
		}
	}

	public void testNewInstance_Interface() {
		Object o = ClazzHelper.newInstance(IClazz.class);
		want.string(o.getClass().getName()).contains("$Proxy");
	}

	public void testNewInstance_DefautClazz() {
		DefautClazz o = ClazzHelper.newInstance(DefautClazz.class);
		want.string(o.name).isEqualTo("default");
	}

	public void testNewInstance_ParamClazz() {
		ParamClazz o = ClazzHelper.newInstance(ParamClazz.class);
		want.string(o.name).isNull();
	}

	public void testNewInstance_PrivateClazz() {
		PrivateClazz o = ClazzHelper.newInstance(PrivateClazz.class);
		want.string(o.name).isEqualTo("private");
	}

	public void testNewInstance_SubPrivateClazz() {
		SubPrivateClazz o = ClazzHelper.newInstance(SubPrivateClazz.class);
		want.string(o.name).isEqualTo("private");
	}
}

abstract class AbstractClazz {

}

interface IClazz {

}

class DefautClazz {
	String name = null;

	public DefautClazz() {
		this.name = "default";
	}
}

class ParamClazz {
	String name = null;

	public ParamClazz(String name) {
		this.name = name;
	}
}

class PrivateClazz {
	String name = null;

	protected PrivateClazz() {
		this.name = "private";
	}
}

class SubPrivateClazz extends PrivateClazz {

}
