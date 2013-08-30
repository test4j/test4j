package org.jtester.json.decoder.array;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jtester.json.JSON;
import org.jtester.json.decoder.CollectionDecoder;
import org.jtester.json.decoder.ForTestType;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes" })
@Test(groups = { "jtester", "json" })
public class CollectionDecoderTest extends JTester {

	@Test
	public void testParseFromJSONArray() {
		String json = "[value1,value2,value3]";
		List list = JSON.toObject(json, ArrayList.class);
		want.collection(list).sizeEq(3).hasAllItems("value1", "value2", "value3");
	}

	public void testDecode_RefValue() {
		String json = "[{#class:org.jtester.json.encoder.beans.test.User@12c8fa8,id:12,name:'darui.wu',age:0,salary:0,isFemale:false},{#refer:@12c8fa8}]";
		List<User> list = JSON.toObject(json, ArrayList.class);
		want.collection(list).sizeEq(2);
		User u1 = list.get(0);
		User u2 = list.get(1);
		want.object(u1).same(u2);
	}

	@Test
	public void testDecode() throws Exception {
		String json = "[{#class:@12c8fa8,id:12,name:'darui.wu',age:0,salary:0,isFemale:false},{#refer:@12c8fa8}]";
		Type type = ForTestType.getType("userList");
		List<User> list = JSON.toObject(json, type);
		want.collection(list).sizeEq(2);
		User u1 = list.get(0);
		User u2 = list.get(1);
		want.object(u1).same(u2);
	}

	@Test
	public void testAccept() throws Exception {
		Type type = ForTestType.getType("userList");
		boolean isAccepted = CollectionDecoder.toCOLLECTION.accept(type);
		want.bool(isAccepted).is(true);
	}

	@Test(dataProvider = "dataForGetComponent")
	public void testGetComponent(String toTypeName, Class componentType) throws Exception {
		Type toType = ForTestType.getType(toTypeName);
		Type type = reflector.invoke(CollectionDecoder.toCOLLECTION, "getComponent", toType);
		want.object(type).isEqualTo(componentType);
	}

	@DataProvider
	public Iterator dataForGetComponent() {
		return new DataIterator() {
			{
				data("objectList", HashMap.class);
				data("userList", User.class);
			}
		};
	}
}
