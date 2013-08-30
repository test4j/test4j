package org.jtester.module.core;

import org.jtester.module.core.utility.IPropItem;

/**
 * A type for modules that offer services to tests. Before a module is used,
 * {@link #init} will be called so that it can initialize itself. After
 * initialization, {@link #getTestListener()} will be called, so that the module
 * can create a callback that can plug into the test exucution sequence. See
 * {@link TestListener} javadoc for more info.
 * 
 */
public interface Module extends IPropItem {

	/**
	 * 根据jTester的配置信息初始化各个模块的变量<br>
	 * <br>
	 * Initializes the module with the jTester properties.
	 * 
	 */
	void init();

	/**
	 * Gives the module the opportunity to performs initialization that can only
	 * work after all other modules have been initialized
	 */
	void afterInit();

	/**
	 * Creates the test listener for this module.
	 * 
	 * @return The test listener, not null
	 */
	TestListener getTestListener();
}
