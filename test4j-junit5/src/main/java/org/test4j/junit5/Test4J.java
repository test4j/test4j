package org.test4j.junit5;

import org.junit.jupiter.api.extension.ExtendWith;
import org.test4j.module.ICore;
import org.test4j.module.IUtil;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;

/**
 * @author:darui.wu Created by darui.wu on 2019/11/5.
 */
@ExtendWith(Test4JExtension.class)
@Test4JTest
public class Test4J implements ICore, ISpring, IDatabase, IUtil {
}
