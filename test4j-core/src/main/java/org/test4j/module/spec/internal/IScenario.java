package org.test4j.module.spec.internal;

import org.test4j.Context;
import org.test4j.module.spec.SpecModule;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.reflector.DisplayNameFinder;

import java.lang.reflect.Method;

/**
 * 场景描述接口类
 *
 * @author darui.wudr 2013-1-10 下午6:57:56
 */
public interface IScenario {
    /**
     * 测试描述
     *
     * @param description
     * @return
     */
    default IGiven scenario(String description) {
        ScenarioResult scenario = SpecModule.currScenario();
        if (!StringHelper.isBlank(description)) {
            scenario.setScenarioName(description);
        }
        return new ScenarioGiven(scenario);
    }

    /**
     * 测试描述
     *
     * @return
     */
    default IGiven scenario() {
        Method method = Context.currTestMethod();
        return this.scenario(DisplayNameFinder.displayName(method));
    }
}