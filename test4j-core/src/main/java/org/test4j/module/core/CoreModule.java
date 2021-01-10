package org.test4j.module.core;

import org.test4j.module.core.internal.ConfigurationLoader;
import org.test4j.module.core.internal.ModulesLoader;

import java.util.List;

/**
 * test4j的核心类，所有事件监听器的总入口<br>
 */
public class CoreModule {
    /**
     * 初始化test4j,要保证这个方法在使用test4j功能之前被调用
     */
    static boolean hasInitial = false;

    public static void initModules() {
        if (hasInitial) {
            return;
        }
        try {
            hasInitial = true;
            ConfigurationLoader.loading();
            List<Module> modules = ModulesLoader.loading();
            for (Module module : modules) {
                module.afterInit();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }
}