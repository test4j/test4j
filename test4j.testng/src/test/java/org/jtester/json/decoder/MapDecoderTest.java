package org.jtester.json.decoder;

import java.util.HashMap;
import java.util.Map;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.json.JSON;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "serial" })
@Test(groups = { "jtester", "json" })
public class MapDecoderTest extends JTester {

	@Test
	public void testParseFromJSONMap() {
		String json = "{'key2':'value2','key1':'value1'}";
		Map map = JSON.toObject(json, HashMap.class);
		want.map(map).hasEntry("key1", "value1", "key2", "value2");
	}

	public void testToJson() {
		String json = "{{id:1,name:'user1'}:'value1', {id:2,name:'user2'}:'value2'}";

		Map actual = JSON.toObject(json, HashMap.class);
		want.map(actual).sizeEq(2).hasValues("value1", "value2");

		want.collection(actual.keySet()).reflectionEq(new Map[] { new HashMap<String, Object>() {
			{
				this.put("id", 1);
				this.put("name", "user1");
			}

		}, new HashMap<String, Object>() {
			{
				this.put("id", 2);
				this.put("name", "user2");
			}
		} }, EqMode.IGNORE_ORDER);
	}

	public void testDecodePrimitive() {
		String json = "{integer:1, boolean:true}";
		Map map = JSON.toObject(json, HashMap.class);
		want.map(map).reflectionEq(new HashMap<String, Object>() {
			{
				this.put("integer", 1);
				this.put("boolean", true);
			}
		});
	}
}
