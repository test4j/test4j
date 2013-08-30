package org.jtester.tools.reflector;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "jtester")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReflectorTest_NewInstance extends JTester {

	@Test(dataProvider = "instance_data")
	public void testNewInstance(Class claz) {
		Object instance = reflector.newInstance(claz);
		want.object(instance).notNull();
		String result = ((ISayHello) instance).getName();
		want.string(result).isNull();
	}

	@DataProvider
	public Object[][] instance_data() {
		return new Object[][] { { NoDefaultConstructor.class },// <br>
				{ ISayHello.class } // <br>
		};
	}

	@Test
	public void testPrivateConstruction() {
		Object instance = reflector.newInstance(PrivateConstructor.class);
		want.object(instance).notNull();
		String result = ((ISayHello) instance).getName();
		want.string(result).isEqualTo("construction");
	}

	public void testNewInstance_AbstractClazz() {
		try {
			reflector.newInstance(AbstractClazz.class);
			want.fail();
		} catch (Exception e) {
			String message = e.getMessage();
			want.string(message).contains("unsupport").contains("abstract class");
		}
	}
}

class NoDefaultConstructor implements ISayHello {
	private String name = "defualt";

	public NoDefaultConstructor(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

class PrivateConstructor implements ISayHello {
	private String name = "defualt";

	private PrivateConstructor() {
		this.name = "construction";
	}

	public String getName() {
		return name;
	}
}

abstract class AbstractClazz implements ISayHello {
	private String name = "defualt";

	private AbstractClazz(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

interface ISayHello {
	public String getName();
}
