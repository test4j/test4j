package org.jtester.module.inject;

import org.jtester.fortest.beans.User;
import org.jtester.module.ICore;
import org.jtester.module.inject.annotations.Inject;
import org.jtester.module.inject.annotations.TestedObject;
import org.junit.Test;

/**
 * 类InjectModuleTest.java的实现描述
 * 
 * @author darui.wudr 2013-1-9 下午2:50:26
 */
public class InjectModuleTest implements ICore {
    @TestedObject
    User   user     = new User();

    @Inject
    User   assistor = new User("he", "a111");

    @Inject
    String first    = "wu";

    @Test
    public void testFindTestedObjectTargets() throws Exception {
        want.object(user).propertyEq("first", "wu");
        want.string(user.getAssistor().getFirst()).isEqualTo("he");
        want.string(user.getAssistor().getLast()).isEqualTo("a111");
    }
}
