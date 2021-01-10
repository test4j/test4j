package org.test4j.module.core.internal;

import org.test4j.module.core.Module;

import java.util.*;

/**
 * test4j模块管理器<br>
 * A class for holding and retrieving modules.
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
    private List<Module>              modules               = null;

    /**
     * 测试监听器（要保证顺序和价值module的顺序一致）
     */
    private List<ModuleListener> moduleListeners = null;

    private List<ModuleListener> moduleListeners_Reverse = null;

    /**
     * A map containing the test listeners of each module
     */
    private Map<Module, ModuleListener> testListenersMap      = null;

    /**
     * Creates a repository containing the given modules.<br>
     * Creates test listeners for each of the given modules.
     * 
     * @param modules the modules, not null
     */
    private ModulesManager(List<Module> modules) {
        this.modules = modules;
        this.moduleListeners = new ArrayList<ModuleListener>();
        this.moduleListeners_Reverse = new ArrayList<ModuleListener>();
        this.testListenersMap = new HashMap<Module, ModuleListener>();
        for (Module module : modules) {
            ModuleListener listener = module.getTestListener();
            this.moduleListeners.add(listener);
            this.moduleListeners_Reverse.add(listener);
            this.testListenersMap.put(module, listener);
        }
        Collections.reverse(this.moduleListeners_Reverse);
    }

    /**
     * 获得modules的所有测试监听器（顺序和module加载顺序一致）<br>
     * Gets all listeners sequence.
     * 
     * @return the listeners per module, not null
     */
    public static List<ModuleListener> getTestListeners() {
        if (modulesManager == null) {
            throw new RuntimeException("there are some error before test4j loading, modules haven't been loaded.");
        } else {
            return modulesManager.moduleListeners;
        }
    }

    /**
     * 获得modules的所有测试监听器（顺序和module加载顺序相反）<br>
     * Gets all listeners sequence.
     * 
     * @return the listeners per module, not null
     */
    public static List<ModuleListener> getTestListeners_Reverse() {
        if (modulesManager == null) {
            throw new RuntimeException("there are some error before test4j loading, modules haven't been loaded.");
        } else {
            return modulesManager.moduleListeners_Reverse;
        }
    }
}