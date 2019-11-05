package org.test4j.junit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.test4j.module.ICore;
import org.test4j.module.core.utility.JMockitHelper;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/11/5.
 */
@ExtendWith(Test4JExtension.class)
public class Test4J implements ICore, ISpring, IDatabase {
    static {
        JMockitHelper.getJMockitJavaagentHit();
    }
}
