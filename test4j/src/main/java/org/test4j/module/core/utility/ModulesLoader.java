package org.test4j.module.core.utility;

import static org.test4j.module.core.internal.IPropItem.PROPKEY_MODULES;
import static org.test4j.tools.commons.ClazzHelper.createInstanceOfType;

import java.util.ArrayList;
import java.util.List;

import org.test4j.exception.Test4JException;
import org.test4j.module.core.Module;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.StringHelper;

/**
 * A class for loading test4j modules.
 * <p/>
 * The core names set by the {@link #PROPKEY_MODULES} property which modules
 * will be loaded. <br>
 * These names can then be used to construct properties that define the
 * classnames and optionally the dependencies of these modules. E.g.
 *
 * <pre>
 * <code>
 * test4j.modules=a, b, c, d
 * test4j.modules.a.className= org.test4j.core.AModule
 * test4j.modules.a.enabled= false
 * </code>
 * </pre>
 * <p>
 * The above configuration will load 3 core classes A, B and C and will always
 * perform processing in order C, B, A.
 * <p/>
 */
public class ModulesLoader {

    /**
     * First part of all core specific properties.
     */
    public static final String PROPKEY_MODULE_PREFIX = "test4j.module.";

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
     * Loading all test4j modules which are enabled and available.
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
                    throw new Test4JException("Unable to load core. Module class is not of type Test4JModule: "
                            + className);
                }
                ((Module) module).init();// initialize module
                modules.add((Module) module);
            } catch (Throwable t) {
                String error = "An exception occured during the loading of core module " + moduleName
                        + " with module class name " + className;
                // throw new Test4JException(error, t);
                System.out.println(error);
            }
        }
        ModulesManager.initManager(modules);
        return modules;
    }

    /**
     * 过滤失效的模块，保持模块序列 Removes all modules that have a value false for the
     * enabled property.
     *
     * @param modules the all module names, not null
     * @return the enabled module name
     */
    private static List<String> filterModules(List<String> modules) {
        List<String> enabledModules = new ArrayList<>();

        for (String module : modules) {
            boolean isEnabled = ConfigHelper.getBoolean(
                    PROPKEY_MODULE_PREFIX + module + PROPKEY_MODULE_SUFFIX_ENABLED,
                    true);
            if (!isEnabled) {
                continue;
            }
            String moduleEnabledClazz = ConfigHelper.getString(PROPKEY_MODULE_PREFIX + module + PROPKEY_MODULE_ENABLED_CLASS);
            if (StringHelper.isBlankOrNull(moduleEnabledClazz)) {
                enabledModules.add(module);
                continue;
            }
            boolean clazzAvailable = ClazzHelper.isClassAvailable(moduleEnabledClazz);
            if (clazzAvailable) {
                enabledModules.add(module);
            } else {
                MessageHelper.warn(String.format(
                        "can't find class %s in classpath, so disabled module[%s].",
                        moduleEnabledClazz, module));
            }
        }
        return enabledModules;
    }
}
