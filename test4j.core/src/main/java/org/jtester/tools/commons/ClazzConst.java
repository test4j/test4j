package org.jtester.tools.commons;

/**
 * 框架中用到的类名称常量
 * 
 * @author darui.wudr
 * 
 */
public interface ClazzConst {
	/**
	 * Spring 事务管理类名称
	 */
	String Spring_PlatformTransactionManager = "org.springframework.transaction.PlatformTransactionManager";

	/**
	 * Spring 数据源工具类类名
	 */
	String Spring_DataSourceUtils = "org.springframework.jdbc.datasource.DataSourceUtils";

	/**
	 * jsr235 定义的@Resource类名
	 */
	String Javax_Resource_Annotation = "javax.annotation.Resource";

	/**
	 * Spring @AutoWired Annotation类类名
	 */
	String Spring_Autowired_Annotation = "org.springframework.beans.factory.annotation.Autowired";
}
