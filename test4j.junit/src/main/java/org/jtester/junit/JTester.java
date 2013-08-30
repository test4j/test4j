package org.jtester.junit;

import org.junit.runner.RunWith;
import org.test4j.module.ICore;
import org.test4j.module.database.IDatabase;
import org.test4j.module.jmockit.IMockict;
import org.test4j.module.spring.ISpring;

@RunWith(JTesterRunner.class)
public interface JTester extends ICore, IMockict, ISpring, IDatabase {
}
