package org.jtester.tools.commons;

import java.lang.reflect.Field;
import java.util.List;

import org.jtester.fortest.beans.Manager;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.FieldHelper;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class FieldHelperTest extends JTester {

	@Test(dataProvider = "getFieldData")
	public void testGetField(String fieldname, String value) throws IllegalArgumentException, IllegalAccessException {
		Object target = new ChildClaz();
		Field field = FieldHelper.getField(ChildClaz.class, fieldname);
		field.setAccessible(true);
		String result = (String) field.get(target);

		want.string(result).isEqualTo(value);
	}

	@Test(expectedExceptions = NoSuchFieldRuntimeException.class)
	public void testGetField_NoSuchField() {
		FieldHelper.getField(ChildClaz.class, "no_such_field");
	}

	@DataProvider
	public Object[][] getFieldData() {
		return new Object[][] { { "static_field", "ChildClaz" }, // <br>
				{ "public_field", "ChildClaz" }, // <br>
				{ "private_field", "ChildClaz" }, // <br>

				{ "parent_static_field", "ParentClaz" }, // <br>
				{ "parent_public_field", "ParentClaz" }, // <br>
				{ "parent_private_field", "ParentClaz" } // <br>
		};
	}

	@SuppressWarnings("unused")
	public static class ParentClaz {
		static String static_field = "ParentClaz";

		public String public_field = "ParentClaz";

		private String private_field = "ParentClaz";
		// only parent have
		static String parent_static_field = "ParentClaz";

		public String parent_public_field = "ParentClaz";

		private String parent_private_field = "ParentClaz";
	}

	@SuppressWarnings("unused")
	public static class ChildClaz extends ParentClaz {
		static String static_field = "ChildClaz";

		public String public_field = "ChildClaz";

		private String private_field = "ChildClaz";
	}

	@Test
	public void testGetAllFields() {
		List<Field> fields = ClazzHelper.getAllFields(Manager.class, null, false, false, false);
		want.collection(fields).sizeEq(4)
				.propertyEq("name", new String[] { "secretary", "phoneNumber", "name", "date" });
	}
}
