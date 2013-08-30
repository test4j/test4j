package org.jtester.module.core.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.module.JTesterException;
import org.jtester.module.core.Module;
import org.jtester.module.core.TestListener;
import org.jtester.tools.commons.ClazzHelper;

/**
 * jTester模块管理器<br>
 * A class for holding and retrieving modules.
 * 
 */
@SuppressWarnings({ "unchecked" })
public class ModulesManager {

	private static ModulesManager modulesManager = null;

	/**
	 * 初始化Modules的管理器
	 * 
	 * @param modules
	 */
	public static final void initManager(List<Module> modules) {
		modulesManager = new ModulesManager(modules);
	}

	/**
	 * 所有模块，（要保证顺序和价值module的顺序一致）
	 */
	private List<Module> modules = null;

	/**
	 * 测试监听器（要保证顺序和价值module的顺序一致）
	 */
	private List<TestListener> testListeners = null;

	private List<TestListener> testListeners_Reverse = null;

	/**
	 * A map containing the test listeners of each module
	 */
	private Map<Module, TestListener> testListenersMap = null;

	/**
	 * Creates a repository containing the given modules.<br>
	 * Creates test listeners for each of the given modules.
	 * 
	 * @param modules
	 *            the modules, not null
	 */
	private ModulesManager(List<Module> modules) {
		this.modules = modules;
		this.testListeners = new ArrayList<TestListener>();
		this.testListeners_Reverse = new ArrayList<TestListener>();
		this.testListenersMap = new HashMap<Module, TestListener>();
		for (Module module : modules) {
			TestListener listener = module.getTestListener();
			this.testListeners.add(listener);
			this.testListeners_Reverse.add(listener);
			this.testListenersMap.put(module, listener);
		}
		Collections.reverse(this.testListeners_Reverse);
	}

	public static ModulesManager instance() {
		if (modulesManager == null) {
			throw new RuntimeException("there are some error before jTester loading, modules haven't been loaded.");
		}
		return modulesManager;
	}

	/**
	 * Gets all modules.
	 * 
	 * @return the modules, not null
	 */
	public static List<Module> getModules() {
		if (modulesManager == null) {
			throw new RuntimeException("there are some error before jTester loading, modules haven't been loaded.");
		}
		return modulesManager.modules;
	}

	/**
	 * Gets the listener corresponding to the given module.
	 * 
	 * @param module
	 *            the module, not null
	 * @return the listener, null if the module could not be found
	 */
	public static TestListener getTestListener(Module module) {
		if (modulesManager == null) {
			throw new RuntimeException("there are some error before jTester loading, modules haven't been loaded.");
		}
		return modulesManager.testListenersMap.get(module);
	}

	/**
	 * 获得modules的所有测试监听器（顺序和module加载顺序一致）<br>
	 * Gets all listeners sequence.
	 * 
	 * @return the listeners per module, not null
	 */
	public static List<TestListener> getTestListeners() {
		if (modulesManager == null) {
			throw new RuntimeException("there are some error before jTester loading, modules haven't been loaded.");
		} else {
			return modulesManager.testListeners;
		}
	}

	/**
	 * 获得modules的所有测试监听器（顺序和module加载顺序相反）<br>
	 * Gets all listeners sequence.
	 * 
	 * @return the listeners per module, not null
	 */
	public static List<TestListener> getTestListeners_Reverse() {
		if (modulesManager == null) {
			throw new RuntimeException("there are some error before jTester loading, modules haven't been loaded.");
		} else {
			return modulesManager.testListeners_Reverse;
		}
	}

	/**
	 * 返回module实例<br>
	 * Gets the modules that is of the given type or a sub-type.<br>
	 * A JTesterException is thrown when there is not exactly 1 possible match.
	 * 
	 * @param <T>
	 *            The module type
	 * @param type
	 *            the module type, not null
	 * @return the module, not null
	 */
	public static <T extends Module> T getModuleInstance(Class<T> type) {
		List<T> modulesOfType = getModulesOfType(type);
		if (modulesOfType.size() > 1) {
			throw new JTesterException("More than one module found of type " + type.getName());
		}
		if (modulesOfType.size() < 1) {
			throw new JTesterException("No module found of type " + type.getName());
		}
		return modulesOfType.get(0);
	}

	/**
	 * Gets all modules that are of the given type or a sub-type.
	 * 
	 * @param <T>
	 *            The module type
	 * @param type
	 *            the type, not null
	 * @return the modules, an empty list if none found
	 */
	private static <T> List<T> getModulesOfType(Class<T> type) {
		if (modulesManager == null) {
			throw new RuntimeException("there are some error before jTester loading, modules haven't been loaded.");
		}
		List<T> result = new ArrayList<T>();
		for (Module module : modulesManager.modules) {
			if (type.isAssignableFrom(module.getClass())) {
				result.add((T) module);
			}
		}
		return result;
	}

	/**
	 * Checks whether a module of a type with the given class name exists. The
	 * class name can also be the super-type of an existing module.
	 * 
	 * @param fullyQualifiedClassName
	 *            The class name, not null
	 * @return True if the module exists and is enabled
	 */
	public boolean isModuleEnabled(String fullyQualifiedClassName) {

		Class<? extends Module> moduleClass;
		try {
			moduleClass = ClazzHelper.getClazz(fullyQualifiedClassName);
		} catch (Throwable e) {
			// class could not be loaded
			return false;
		}
		return isModuleEnabled(moduleClass);
	}

	/**
	 * Checks whether a module of a type exists. The class an also be the
	 * super-type of an existing module.
	 * 
	 * @param moduleClass
	 *            The class, not null
	 * @return True if the module exists and is enabled
	 */
	public static boolean isModuleEnabled(Class<? extends Module> moduleClass) {
		List<? extends Module> modulesOfType = getModulesOfType(moduleClass);
		if (modulesOfType.size() > 1) {
			throw new JTesterException("More than one module found of type " + moduleClass.getName());
		}
		return modulesOfType.size() == 1;
	}

}
