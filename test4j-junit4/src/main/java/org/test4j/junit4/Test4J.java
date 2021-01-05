package org.test4j.junit4;

import org.junit.runner.RunWith;
import org.test4j.module.ICore;
import org.test4j.module.IUtil;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;

@RunWith(Test4JProxyRunner.class)
public abstract class Test4J implements ICore, ISpring, IDatabase, IUtil {
}
