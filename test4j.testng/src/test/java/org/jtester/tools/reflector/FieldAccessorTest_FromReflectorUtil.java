package org.jtester.tools.reflector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.fortest.beans.Address;
import org.jtester.fortest.beans.Employee;
import org.jtester.fortest.beans.User;
import org.jtester.fortest.reflector.ForReflectUtil;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.FieldHelper;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.jtester.tools.reflector.PropertyAccessor;
import org.testng.annotations.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Test(groups = { "JTester" })
public class FieldAccessorTest_FromReflectorUtil extends JTester {

	public void setFieldValue() {
		Employee employee = new Employee();
		want.object(employee.getName()).isNull();
		FieldHelper.setFieldValue(employee, "name", "my name");
		want.object(employee).propertyEq("name", "my name");
	}

	@Test(expectedExceptions = { NoSuchFieldRuntimeException.class })
	public void setFieldValue_exception() {
		Employee employee = new Employee();
		want.object(employee.getName()).isNull();
		FieldHelper.setFieldValue(employee, "name1", "my name");
	}

	@Test(expectedExceptions = { RuntimeException.class })
	public void setFieldValue_AssertError() {
		FieldHelper.setFieldValue(null, "name1", "my name");
	}

	public void getFieldValue() {
		Employee employee = new Employee();
		employee.setName("test name");
		Object name = FieldHelper.getFieldValue(employee, "name");
		want.object(name).clazIs(String.class);
		want.string(name.toString()).isEqualTo("test name");
	}

	@Test
	public void getFieldValue_exception() {
		try {
			Employee employee = new Employee();
			employee.setName("test name");
			FieldHelper.getFieldValue(employee, "name1");
			want.fail();
		} catch (Throwable e) {
			String message = e.getMessage();
			want.string(message).start("No such field:");
		}
	}

	@Test(expectedExceptions = { RuntimeException.class })
	public void getFieldValue_AssertError() {
		FieldHelper.getFieldValue(null, "name1");
	}

	@Test(expectedExceptions = { NoSuchFieldRuntimeException.class })
	public void getFieldValue_AssertError2() {
		FieldHelper.getStaticFieldValue(Employee.class, "name1");
	}

	@Test
	public void testGetArrayItemProperty() {
		List<?> values = PropertyAccessor.getArrayItemProperty(
				Arrays.asList(new User("ddd", "eeee"), new User("ccc", "dddd")), "first");
		want.collection(values).reflectionEq(new String[] { "ddd", "ccc" });
	}

	@Test(description = "数组类型")
	public void testGetArrayItemProperty_Array() {
		List<?> values = PropertyAccessor.getArrayItemProperty(new User[] { new User("ddd", "eeee"),
				new User("ccc", "dddd") }, "first");
		want.collection(values).reflectionEq(new String[] { "ddd", "ccc" });
	}

	@Test(description = "单值")
	public void testGetArrayItemProperty_SingleValue() {
		List<?> values = PropertyAccessor.getArrayItemProperty(new User("ddd", "eeee"), "first");
		want.collection(values).reflectionEq(new String[] { "ddd" });
	}

	@Test(description = "Map类型")
	public void testGetArrayItemProperty_Map() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("first", "ddd");

		List<?> values = PropertyAccessor.getArrayItemProperty(map, "first");
		want.collection(values).reflectionEq(new String[] { "ddd" });
	}

	@Test(description = "Map类型_集合")
	public void testGetArrayItemProperty_MapList() {
		List list = new ArrayList();
		for (int i = 0; i < 2; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("first", "ddd");
			list.add(map);
		}
		List<?> values = PropertyAccessor.getArrayItemProperty(list, "first");
		want.collection(values).reflectionEq(new String[] { "ddd", "ddd" });
	}

	@Test
	public void testGetArrayItemProperties() {
		Object[][] values = PropertyAccessor.getArrayItemProperties(
				Arrays.asList(new User("ddd", "eeee"), new User("ccc", "dddd")), new String[] { "first", "last" });
		want.array(values).reflectionEq(new String[][] { { "ddd", "eeee" }, { "ccc", "dddd" } });
	}

	@Test(description = "数组类型")
	public void testGetArrayItemProperties_Array() {
		Object[][] values = PropertyAccessor.getArrayItemProperties(new User[] { new User("ddd", "eeee"),
				new User("ccc", "dddd") }, new String[] { "first", "last" });
		want.array(values).reflectionEq(new String[][] { { "ddd", "eeee" }, { "ccc", "dddd" } });
	}

	@Test(description = "单值")
	public void testGetArrayItemProperties_SingleValue() {
		Object[][] values = PropertyAccessor.getArrayItemProperties(new User("ddd", "eeee"), new String[] { "first",
				"last" });
		want.array(values).reflectionEq(new String[][] { { "ddd", "eeee" } });
	}

	@Test(description = "Map类型")
	public void testGetArrayItemProperties_Map() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("first", "ddd");
		map.put("last", "eeee");

		Object[][] values = PropertyAccessor.getArrayItemProperties(map, new String[] { "first", "last" });
		want.array(values).reflectionEq(new String[][] { { "ddd", "eeee" } });
	}

	@Test(description = "Map类型集合")
	public void testGetArrayItemProperties_MapList() {
		List list = new ArrayList();
		for (int i = 0; i < 2; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("first", "ddd");
			map.put("last", "eeee");
			list.add(map);
		}
		Object[][] values = PropertyAccessor.getArrayItemProperties(list, new String[] { "first", "last" });
		want.array(values).reflectionEq(new String[][] { { "ddd", "eeee" }, { "ddd", "eeee" } });
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void testGetProperty() {
		PropertyAccessor.getProperty(null, "dd");
	}

	@Test(description = "对象是Map")
	public void testGetProperty_Map() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("wikiName", "eeee");
		String value = (String) PropertyAccessor.getProperty(map, "wikiName");
		want.string(value).isEqualTo("eeee");

		try {
			PropertyAccessor.getProperty(map, "kkkk");
			want.fail();
		} catch (Exception e) {
			want.string(e.getMessage()).contains("no key[kkkk]");
		}
	}

	@Test(description = "没有这个属性")
	public void testGetProperty_NoProp() {
		try {
			PropertyAccessor.getProperty(item, "dde");
			want.fail();
		} catch (Throwable e) {
			String error = e.getMessage();
			want.string(error).start("No such field:");
		}
	}

	ForReflectUtil item = new ForReflectUtil("first name", "last name");

	@Test(description = "Get方法可以取到值")
	public void testGetProperty_GetMethod() {
		String first = (String) PropertyAccessor.getProperty(item, "first");
		want.string(first).isEqualTo("first name");
	}

	@Test(description = "方法可以取到值_但方法在父类")
	public void testGetProperty_GetMethodInParentClass() {
		SubForReflectUtil item1 = new SubForReflectUtil("first name", "last name");
		String field = (String) PropertyAccessor.getProperty(item1, "myName");
		want.string(field).isEqualTo("first name,last name");
	}

	public void testGetProperty_IsMethod() {
		boolean isMan = (Boolean) PropertyAccessor.getProperty(item, "man");
		want.bool(isMan).is(true);
	}

	@Test(description = "只能通过直接取字段")
	public void testGetProperty_NotGetMethod() {
		String field = (String) PropertyAccessor.getProperty(item, "noGetMethod");
		want.string(field).isEqualTo("no get method field");
	}

	@Test(description = "Get方法有逻辑")
	public void testGetProperty_GetMethodHasLogical() {
		String field = (String) PropertyAccessor.getProperty(item, "myName");
		want.string(field).isEqualTo("first name,last name");
	}

	@Test(description = "只能通过直接取字段_且字段在父类中")
	public void testGetProperty_FieldInParentClass() {
		SubForReflectUtil item1 = new SubForReflectUtil("first name", "last name");
		String field = (String) PropertyAccessor.getProperty(item1, "noGetMethod");
		want.string(field).isEqualTo("no get method field");
	}

	public static class SubForReflectUtil extends ForReflectUtil {
		public SubForReflectUtil(String first, String last) {
			super(first, last);
		}
	}

	@Test(description = "单值对象_且属性值非集合")
	public void testGetArrayOrItemProperty_SingleValue_And_PropNotList() {
		Collection values = PropertyAccessor.getArrayOrItemProperty(new User("ddd", "ddd"), "first");
		want.collection(values).sizeEq(1).reflectionEq(new String[] { "ddd" });
	}

	@Test(description = "单值对象_且属性值为集合")
	public void testGetArrayOrItemProperty_SingleValue_PropIsList() {
		User user = new User("ddd", "ddd");
		user.setAddresses(Arrays.asList(new Address("aaa"), new Address("bbb")));
		Collection values = PropertyAccessor.getArrayOrItemProperty(user, "addresses");
		want.collection(values).sizeEq(2).reflectionEq(new Address[] { new Address("aaa"), new Address("bbb") });
	}
}
