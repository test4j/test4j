package org.test4j.spec;

import java.util.Set;

import org.junit.Test;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.module.ICore;
import org.test4j.spec.JSpecExecutorFactory;

/**
 * @author darui.wudr 2013-1-10 下午9:06:54
 */
public class JSpecFactoryTest implements ICore {

    @Test
    public void testFindScenarioRunIndex() throws Exception {
        System.setProperty(JSpecExecutorFactory.SCENARIO_RUN_INDEX, "1,2");
        Set<Integer> indexs = JSpecExecutorFactory.findScenarioRunIndex();
        want.list(indexs).reflectionEq(new Integer[] { 1, 2 }, EqMode.IGNORE_ORDER);
    }
}
