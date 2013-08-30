package org.test4j.junit;

import org.junit.runner.RunWith;
import org.test4j.module.ICore;
import org.test4j.module.database.IDatabase;
import org.test4j.module.jmockit.IMockict;
import org.test4j.module.spring.ISpring;

@RunWith(Test4JRunner.class)
public interface Test4J extends ICore, IMockict, ISpring, IDatabase {
}
