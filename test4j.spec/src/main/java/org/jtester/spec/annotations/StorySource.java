package org.jtester.spec.annotations;

/**
 * Story文件内容来源
 * 
 * @author darui.wudr 2012-7-3 上午10:55:21
 */
public enum StorySource {
	/**
	 * 由配置文件决定
	 */
	Default,
	/**
	 * 数据来源classpath
	 */
	ClassPath,
	/**
	 * 数据来源于Titian系统,需要在jtester.properties配置titian系统url和projectID
	 */
	Titian;
}