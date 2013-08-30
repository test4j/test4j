package org.jtester.tools.reflector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jtester.json.encoder.beans.test.User;
import org.jtester.testng.JTester;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.jtester.tools.reflector.PropertyAccessor;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
@Test(groups = "jtester")
public class PropertyAccessorTest extends JTester {

	@Test(dataProvider = "testGetPropertyDatas")
	public void testGetPropertyByOgnl(Object target, String property, String expected) {
		String value = (String) PropertyAccessor.getPropertyByOgnl(target, property, true);
		want.string(value).isEqualTo(expected);
	}

	@DataProvider
	public Iterator testGetPropertyDatas() {
		return new DataIterator() {
			{
				data(new HashMap() {
					{
						this.put("key1", "value1");
						this.put("key2", "value2");
					}
				}, "key1", "value1");

				data(new HashMap() {
					{
						this.put("key1", User.newInstance(12, "darui.wu"));
						this.put("key2", "value2");
					}
				}, "key1.name", "darui.wu");

				data(User.newInstance(12, "darui.wu"), "name", "darui.wu");

				data(new HashMap() {
					{
						put("map1.key1", new HashMap() {
							{
								put("map2.key2", "ok");
							}
						});
					}
				}, "map1.key1.map2.key2", "ok");
			}
		};
	}

	@Test(expectedExceptions = NoSuchFieldRuntimeException.class)
	public void testGetPropertyByOgnl_NoKey_Failure() {
		Map target = new HashMap() {
			{
				put("map1.key1", new HashMap() {
					{
						put("map2.key2", "ok");
					}
				});
			}
		};
		PropertyAccessor.getPropertyByOgnl(target, "map1.key1.map2.key1", true);
	}

	public void testGetPropertyByOgnl_NoKey_Success() {
		Map target = new HashMap() {
			{
				put("map1.key1", new HashMap() {
					{
						put("map2.key2", "ok");
					}
				});
			}
		};
		Object value = PropertyAccessor.getPropertyByOgnl(target, "map1.key1.map2.key1", false);
		want.object(value).same(target);
	}
}
