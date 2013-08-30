package org.jtester.junit;

import org.jtester.module.ICore;
import org.jtester.module.database.IDatabase;
import org.jtester.module.jmockit.IMockict;
import org.jtester.module.spring.ISpring;
import org.junit.runner.RunWith;

@RunWith(JTesterRunner.class)
public interface JTester extends ICore, IMockict, ISpring, IDatabase {
}
