package org.test4j.junit.suitetest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.test4j.junit.suitetest.suite.Groups;

@RunWith(Groups.class)
@Suite.SuiteClasses({ ClasspathTest.class })
public abstract class GroupTest {

}
