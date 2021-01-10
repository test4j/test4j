package org.test4j.module.inject;

import org.junit.jupiter.api.Test;
import org.test4j.Test4J;
import org.test4j.module.fortest.User;
import org.test4j.module.inject.inject.Inject;
import org.test4j.module.inject.inject.Injected;

/**
 * 类InjectModuleTest.java的实现描述
 *
 * @author darui.wudr 2013-1-9 下午2:50:26
 */
public class InjectModuleTest implements Test4J {
    @Injected
    User user = new User();

    @Inject
    User assistor = new User("he", "a111");

    @Inject
    String first = "wu";

    @Test
    public void testFindTestedObjectTargets() throws Exception {
        want.object(user).eqByProperties("first", "wu");
        want.string(user.getAssistor().getFirst()).isEqualTo("he");
        want.string(user.getAssistor().getLast()).isEqualTo("a111");
    }
}