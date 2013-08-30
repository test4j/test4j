package org.test4j.testng;

import org.test4j.module.ICore;
import org.test4j.module.database.IDatabase;
import org.test4j.module.jmockit.IMockict;
import org.test4j.module.spring.ISpring;
import org.testng.annotations.Test;

@Test(groups = "all-test")
public abstract class Test4J extends Test4JCore implements ICore, IMockict, ISpring, IDatabase {
}
