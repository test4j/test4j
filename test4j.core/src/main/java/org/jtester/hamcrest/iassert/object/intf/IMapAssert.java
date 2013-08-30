package org.jtester.hamcrest.iassert.object.intf;

import java.util.Map;

import org.jtester.hamcrest.iassert.common.intf.IBaseAssert;
import org.jtester.hamcrest.iassert.common.intf.IReflectionAssert;
import org.jtester.hamcrest.iassert.common.intf.ISizedAssert;

/**
 * map对象断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface IMapAssert extends IBaseAssert<Map<?, ?>, IMapAssert>, ISizedAssert<IMapAssert>,
		IReflectionAssert<IMapAssert> {
	/**
	 * 断言map包含参数中列出的key值
	 * 
	 * @param key
	 *            期望key值
	 * @param keys
	 *            期望key值
	 * @return
	 */
	IMapAssert hasKeys(Object key, Object... keys);

	/**
	 * 断言map包含参数中列出的值对象
	 * 
	 * @param value
	 *            期望的值
	 * @param values
	 *            期望的值
	 * @return
	 */
	IMapAssert hasValues(Object value, Object... values);

	/**
	 * 断言map中包含kay:value值对
	 * 
	 * @param key
	 * @param value
	 * @param objects
	 * @return
	 */
	IMapAssert hasEntry(Object key, Object value, Object... objects);

	/**
	 * 断言map中包含kay:value值对
	 * 
	 * @param entry
	 * @param entries
	 * @return
	 */
	IMapAssert hasEntry(Map.Entry<?, ?> entry, Map.Entry<?, ?>... entries);
}
