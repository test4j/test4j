package org.jtester.module.core.utility;

import static org.jtester.module.core.utility.IPropItem.PROPKEY_MODULES;
import static org.jtester.tools.commons.ClazzHelper.createInstanceOfType;

import java.util.ArrayList;
import java.util.List;

import org.jtester.module.JTesterException;
import org.jtester.module.core.Module;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.ConfigHelper;
import org.jtester.tools.commons.StringHelper;

/**
 * A class for loading jTester modules.
 * <p/>
 * The core names set by the {@link #PROPKEY_MODULES} property which modules
 * will be loaded. <br>
 * These names can then be used to construct properties that define the
 * classnames and optionally the dependencies of these modules. E.g.
 * 
 * <pre>
 * <code>
 * jtester.modules=a, b, c, d
 * jtester.modules.a.className= org.jtester.core.AModule
 * jtester.modules.a.enabled= false
 * </code>
 * </pre>
 * 
 * The above configuration will load 3 core classes A, B and C and will always
 * perform processing in order C, B, A.
 * <p/>
 */
public class ModulesLoader {

	/**
	 * First part of all core specific properties.
	 */
	public static final String PROPKEY_MODULE_PREFIX = "jtester.module.";

	/**
	 * Last part of the core specific property that specifies whether the core
	 * should be loaded.
	 */
	public static final String PROPKEY_MODULE_SUFFIX_ENABLED = ".enabled";

	/**
	 * 特定模块类属性后缀<br>
	 * 如果特定的模块类在classpath中不存在，则不加载该模块
	 */
	public static final String PROPKEY_MODULE_ENABLED_CLASS = ".enabled.class";

	/**
	 * Last part of the core specific property that specifies the classname of
	 * the core.
	 */
	public static final String PROPKEY_MODULE_SUFFIX_CLASS_NAME = ".className";

	/**
	 * Loading all jTester modules which are enabled and available.
	 * 
	 * @return the modules which have loaded, not null
	 */
	public static List<Module> loading() {
		List<String> moduleNames = ConfigHelper.getStringList(PROPKEY_MODULES);

		// remove all disable modules
		moduleNames = filterModules(moduleNames);

		// Create core instances in the correct sequence
		List<Module> modules = new ArrayList<Module>();
		for (String moduleName : moduleNames) {
			// get module class name
			String className = ConfigHelper.getString(PROPKEY_MODULE_PREFIX + moduleName
					+ PROPKEY_MODULE_SUFFIX_CLASS_NAME);
			try {
				// create module instance
				Object module = createInstanceOfType(className);
				if (!(module instanceof Module)) {
					throw new JTesterException("Unable to load core. Module class is not of type JTesterModule: "
							+ className);
				}
				((Module) module).init();// initialize module
				modules.add((Module) module);
			} catch (Throwable t) {
				String error = "An exception occured during the loading of core module " + moduleName
						+ " with module class name " + className;
				// throw new JTesterException(error, t);
				System.out.println(error);
			}
		}
		ModulesManager.initManager(modules);
		return modules;
	}

	/**
	 * 
	 * 过滤失效的模块，保持模块序列
	 * 
	 * Removes all modules that have a value false for the enabled property.
	 * 
	 * @param modules
	 *            the all module names, not null
	 * @return the enabled module name
	 */
	private static List<String> filterModules(List<String> modules) {
		List<String> enabledModules = new ArrayList<String>();
		String dbType = ConfigHelper.databaseType();

		for (String module : modules) {
			if (StringHelper.isBlankOrNull(dbType)) {
				if (module.equalsIgnoreCase("database"))
					continue;
				if (module.equalsIgnoreCase("dbfit"))
					continue;
			}
			boolean isEnabled = ConfigHelper.getBoolean(PROPKEY_MODULE_PREFIX + module
					+ PROPKEY_MODULE_SUFFIX_ENABLED, true);
			if (isEnabled == false) {
				continue;
			}
			String moduleEnabledClazz = ConfigHelper.getString(PROPKEY_MODULE_PREFIX + module
					+ PROPKEY_MODULE_ENABLED_CLASS);
			if (StringHelper.isBlankOrNull(moduleEnabledClazz)) {
				enabledModules.add(module);
				continue;
			}
			boolean clazzAvailable = ClazzHelper.isClassAvailable(moduleEnabledClazz);
			if (clazzAvailable) {
				enabledModules.add(module);
			} else {
				MessageHelper.warn(String.format("can't find class %s in classpath, so disabled module[%s]",
						clazzAvailable, module));
			}
		}
		return enabledModules;
	}
}
