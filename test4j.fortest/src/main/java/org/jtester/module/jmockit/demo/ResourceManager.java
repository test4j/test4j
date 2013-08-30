package org.jtester.module.jmockit.demo;

import java.util.Collection;

public interface ResourceManager {
	/**
	 * 初始化.
	 */
	void init();

	/**
	 * 返回资源名为resName的资源列表.
	 * 
	 * @param resName
	 * @return
	 */
	Collection<?> getResList(String resName);

	/**
	 * 返回资源名为resName, 多个key值对应的资源对象
	 * 
	 * @param resName
	 * @param keys
	 * @return
	 */
	Object[] getResItems(java.lang.String resName, java.lang.String[] keys);

	/**
	 * 取得一个资源对象.
	 * 
	 * @param resName
	 * @param key
	 * @return
	 */
	Object getResItem(String resName, String key);

	/**
	 *返回资源名为resName中key值对应的的Option的value.
	 * 
	 * @param resName
	 *            资源名
	 * @param key
	 *            键值
	 * @param language
	 *            语言
	 * @return
	 */
	String getOptionValue(String resName, String key);
}
