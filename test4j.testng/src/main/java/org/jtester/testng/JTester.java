package org.jtester.testng;

import org.jtester.module.ICore;
import org.jtester.module.database.IDatabase;
import org.jtester.module.jmockit.IMockict;
import org.jtester.module.spring.ISpring;
import org.testng.annotations.Test;

@Test(groups = "all-test")
public abstract class JTester extends JTesterCore implements ICore, IMockict, ISpring, IDatabase {
}
