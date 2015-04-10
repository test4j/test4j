package org.test4j.junit.suitetest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.test4j.junit.suitetest.suite.GroupSuite;

@Suite.SuiteClasses({ ClassPathSuiteTest.class })
@RunWith(GroupSuite.class)
public abstract class GroupSuiteTest {

}
